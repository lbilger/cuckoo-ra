/*
 * Copyright (C) 2010 akquinet tech@spree GmbH
 *
 * This file is part of the Cuckoo Resource Adapter for SAP.
 *
 * Cuckoo Resource Adapter for SAP is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Cuckoo Resource Adapter for SAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with Cuckoo Resource Adapter for SAP. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cuckoo.ra.it;

import org.cuckoo.ra.cci.CuckooConnection;
import org.cuckoo.ra.cci.CuckooIndexedRecord;
import org.cuckoo.ra.cci.CuckooMappedRecord;
import org.cuckoo.ra.common.ConnectionMetaDataImpl;
import org.cuckoo.ra.it.transaction.TransactionTestEjb;
import org.cuckoo.ra.it.transaction.TransactionTestEjbBean;
import org.cuckoo.ra.jco.CuckooDestinationDataProvider;
import org.cuckoo.ra.spi.CuckooResourceAdapter;
import org.cuckoo.ra.util.ForwardingList;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

@RunWith( Arquillian.class )
public class CuckooTest
{
    private static final Logger LOG = LoggerFactory.getLogger( CuckooTest.class.getName() );

    private static final String DEPLOYMENT_NAME = "CuckooTestRA";

    private static final String CUSTOMER_NUMBER = "00000001";

    @EJB
    private TransactionTestEjb ejb;

    @Deployment
    public static EnterpriseArchive createDeployment()
    {
        JavaArchive jar = ShrinkWrap.create( JavaArchive.class, DEPLOYMENT_NAME + ".jar" );
        jar.addPackages( false, CuckooResourceAdapter.class.getPackage(), CuckooConnection.class.getPackage(),
                ForwardingList.class.getPackage(), CuckooDestinationDataProvider.class.getPackage(),
                ConnectionMetaDataImpl.class.getPackage() );

        ResourceAdapterArchive rar =
                ShrinkWrap.create( ResourceAdapterArchive.class, DEPLOYMENT_NAME + ".rar" );
        rar.addLibrary( jar );
        rar.addManifestResource( CuckooTest.class.getResource( "/META-INF/ra.xml" ), "ra.xml" );

        final JavaArchive testJar = ShrinkWrap.create( JavaArchive.class, "rartest.jar" )
                .addClasses(
                        TransactionTestEjb.class, TransactionTestEjbBean.class,
                        CuckooTest.class );

        // must be test.ear, otherwise Arquillian does not guess the correct JNDI-name for the EJBs
        EnterpriseArchive ear = ShrinkWrap.create( EnterpriseArchive.class, "test.ear" );
        ear.addModule( "cuckoo-jboss-ds.xml" );
        ear.addApplicationResource( "jboss-app.xml" );
        ear.addModule( rar );
        ear.addModule( testJar );

        return ear;
    }

    @Test
    public void testCallWithoutTransaction() throws ResourceException
    {
        // Changing phone number to new value...
        final String newPhoneNo = "" + new Date().getTime();
        LOG.info( "testTransactionalCallWithoutTransaction(): changing phone number to: " + newPhoneNo );
        MappedRecord record = createInputRecordForChangingCustomerPhoneNumber( newPhoneNo );
        MappedRecord result = ejb.callFunctionWithoutTransaction( record );

        assertNoSapError( result );

        // Test: Phone number should not be changed in SAP
        record = createInputRecordForGettingCustomerData();
        result = ejb.callFunctionWithoutTransaction( record );

        assertNoSapError( result );

        final IndexedRecord customerList = ( IndexedRecord ) result.get( "CUSTOMER_LIST" );
        assertEquals( 1, customerList.size() );
        final MappedRecord customerData = ( MappedRecord ) customerList.get( 0 );
        assertFalse( newPhoneNo.equals( customerData.get( "PHONE" ) ) );
    }


    @Test
    public void testTransactionalCallWithCMT() throws ResourceException
    {
        // Changing phone number to new value...
        final String newPhoneNo = "" + new Date().getTime();
        LOG.info( "testTransactionalCallWithCMT(): changing phone number to: " + newPhoneNo );
        MappedRecord record = createInputRecordForChangingCustomerPhoneNumber( newPhoneNo );
        MappedRecord result = ejb.callFunctionWithContainerManagedTransaction( record );

        assertNoSapError( result );

        // Test: was it really changes and committed?
        record = createInputRecordForGettingCustomerData();
        result = ejb.callFunctionWithoutTransaction( record );

        assertNoSapError( result );

        final IndexedRecord customerList = ( IndexedRecord ) result.get( "CUSTOMER_LIST" );
        assertEquals( 1, customerList.size() );
        final MappedRecord customerData = ( MappedRecord ) customerList.get( 0 );
        assertEquals( newPhoneNo, customerData.get( "PHONE" ) );
    }

    @Test
    public void testTransactionalCallWithCMTAndRollback() throws ResourceException
    {
        // Changing phone number to new value...
        final String newPhoneNo = "" + new Date().getTime();
        LOG.info( "testTransactionalCallWithCMT(): changing phone number to: " + newPhoneNo );
        MappedRecord record = createInputRecordForChangingCustomerPhoneNumber( newPhoneNo );
        try
        {
            ejb.callFunctionWithContainerManagedTransactionAndThrowRuntimeException( record );
            fail();
        }
        catch ( RuntimeException e )
        {
            e.printStackTrace();
            // expected
        }
        // Test: changes should have been rolled back in SAP
        record = createInputRecordForGettingCustomerData();
        MappedRecord result = ejb.callFunctionWithoutTransaction( record );

        assertNoSapError( result );

        final IndexedRecord customerList = ( IndexedRecord ) result.get( "CUSTOMER_LIST" );
        assertEquals( 1, customerList.size() );
        final MappedRecord customerData = ( MappedRecord ) customerList.get( 0 );
        assertFalse( newPhoneNo.equals( customerData.get( "PHONE" ) ) );
    }

    @Test
    public void testTransactionalCallWithLocalTransaction() throws ResourceException
    {
        // Changing phone number to new value...
        final String newPhoneNo = "" + new Date().getTime();
        LOG.info( "testTransactionalCallWithLocalTransaction(): changing phone number to: " + newPhoneNo );
        MappedRecord record = createInputRecordForChangingCustomerPhoneNumber( newPhoneNo );
        MappedRecord result = ejb.callFunctionWithLocalTransaction( record );

        assertNoSapError( result );

        // Test: was it really changes and committed?
        record = createInputRecordForGettingCustomerData();
        result = ejb.callFunctionWithoutTransaction( record );

        assertNoSapError( result );

        final IndexedRecord customerList = ( IndexedRecord ) result.get( "CUSTOMER_LIST" );
        assertEquals( 1, customerList.size() );
        final MappedRecord customerData = ( MappedRecord ) customerList.get( 0 );
        assertEquals( newPhoneNo, customerData.get( "PHONE" ) );
    }

    @SuppressWarnings( "unchecked" )
    private MappedRecord createInputRecordForChangingCustomerPhoneNumber( String newPhoneNo )
    {
        final MappedRecord record = new CuckooMappedRecord( "BAPI_FLCUST_CHANGE" );
        record.put( "CUSTOMERNUMBER", CUSTOMER_NUMBER );
        final MappedRecord customerData = new CuckooMappedRecord( "CUSTOMER_DATA" );
        record.put( "CUSTOMER_DATA", customerData );
        customerData.put( "PHONE", newPhoneNo );
        final MappedRecord customerDataX = new CuckooMappedRecord( "CUSTOMER_DATA_X" );
        record.put( "CUSTOMER_DATA_X", customerDataX );
        customerDataX.put( "PHONE", "X" );
        return record;
    }

    @SuppressWarnings( "unchecked" )
    private MappedRecord createInputRecordForGettingCustomerData()
    {
        final MappedRecord record = new CuckooMappedRecord( "BAPI_FLCUST_GETLIST" );
        final IndexedRecord customerRangeTable = new CuckooIndexedRecord( "CUSTOMER_RANGE" );
        record.put( "CUSTOMER_RANGE", customerRangeTable );
        final MappedRecord customerRange = new CuckooMappedRecord( "CUSTOMER_RANGE" );
        customerRangeTable.add( customerRange );
        customerRange.put( "SIGN", "I" );
        customerRange.put( "OPTION", "EQ" );
        customerRange.put( "LOW", CUSTOMER_NUMBER );
        return record;
    }

    private void assertNoSapError( MappedRecord result )
    {
        final CuckooIndexedRecord returnTable = ( CuckooIndexedRecord ) result.get( "RETURN" );
        for ( Object line : returnTable )
        {
            CuckooMappedRecord returnStructure = ( CuckooMappedRecord ) line;
            final Object errorType = returnStructure.get( "TYPE" );
            if ( "E".equals( errorType ) || "A".equals( errorType ) )
            {
                throw new AssertionError( "SAP returned Error of type " + errorType + " with message:" +
                        returnStructure.get( "MESSAGE" ) );
            }
        }
    }
}
