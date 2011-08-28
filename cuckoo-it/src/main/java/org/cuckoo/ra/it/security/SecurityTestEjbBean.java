/*
 * Copyright (C) 2011 akquinet tech@spree GmbH
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

package org.cuckoo.ra.it.security;

import com.sap.conn.jco.monitor.JCoConnectionData;
import com.sap.conn.jco.monitor.JCoConnectionMonitor;
import org.cuckoo.ra.cci.CuckooMappedRecord;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;
import javax.resource.cci.MappedRecord;
import java.util.ArrayList;
import java.util.List;

@Stateless
@SecurityDomain( "ejb-test" )
public class SecurityTestEjbBean implements SecurityTestEjb
{
    private static final Logger LOG = LoggerFactory.getLogger( SecurityTestEjbBean.class );

    private static final String RA_JNDI_NAME = "java:eis/sap/A12";

    @Resource( mappedName = RA_JNDI_NAME )
    private ConnectionFactory cf;

    @Resource
    private EJBContext ctx;

    public String callFunctionAndReturnEisUser() throws ResourceException
    {
        LOG.info( "Caller principal: " + ctx.getCallerPrincipal() );

//        ApplicationProperties applicationProperties = new ApplicationProperties( "SAPUSER2", "password" );

        final Connection connection = cf.getConnection();
        try
        {
            callSapFunction( connection );
            return getConnectionUserFromJCoMonitor();
        }
        finally
        {
            connection.close();
        }
    }

    private void callSapFunction( Connection connection )
    {
        try
        {
            final MappedRecord input = new CuckooMappedRecord( "RFC_PING" );

            final Interaction interaction = connection.createInteraction();

            try
            {
                interaction.execute( null, input );
            }
            finally
            {
                interaction.close();
            }
        }
        catch ( ResourceException e )
        {
            throw new RuntimeException( "Error calling SAP system", e );
        }
    }

    /**
     * This method is implemented a bit too complicated: Just to be sure it checks whether there
     * really is only one call of RFC_PING function.
     *
     * @return The SAP user that was used to call the ABAP function.
     */
    private String getConnectionUserFromJCoMonitor()
    {
        final List<? extends JCoConnectionData> list = JCoConnectionMonitor.getConnectionsData();

        List<String> usersThatCalledRfcPing = new ArrayList<String>();
        for ( final JCoConnectionData data : list )
        {
            if ( "RFC_PING".equals( data.getFunctionModuleName() ) )
            {
                usersThatCalledRfcPing.add( data.getAbapUser() );
            }
        }

        if ( usersThatCalledRfcPing.size() == 1 )
        {
            return usersThatCalledRfcPing.get( 0 );
        }

        throw new AssertionError(
                "Expected that function RFC_PING was called once, but was called " + usersThatCalledRfcPing.size() +
                        " times with the following SAP users: " + usersThatCalledRfcPing );
    }
}