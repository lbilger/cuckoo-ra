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
package org.cuckoo.ra.spi;

import com.sap.conn.jco.ext.Environment;
import org.cuckoo.ra.cci.CuckooRaMetaData;
import org.cuckoo.ra.cci.CuckooRecordFactory;
import org.cuckoo.ra.jco.CuckooDestinationDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import java.util.Properties;


public class CuckooResourceAdapter implements ResourceAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger( CuckooResourceAdapter.class );

    private final CuckooDestinationDataProvider destinationDataProvider;
    private final CuckooRaMetaData resourceAdapterMetaData;
    private final CuckooRecordFactory recordFactory;

    public CuckooResourceAdapter()
    {
        LOG.trace( "CuckooResourceAdapter.CuckooResourceAdapter()" );
        resourceAdapterMetaData = new CuckooRaMetaData();
        destinationDataProvider = new CuckooDestinationDataProvider();
        recordFactory = new CuckooRecordFactory();
    }

    /**
     * This is called during the activation of a message endpoint.
     *
     * @param endpointFactory a message endpoint factory instance.
     * @param spec            an activation spec JavaBean instance.
     * @throws ResourceException generic exception
     */
    public void endpointActivation( MessageEndpointFactory endpointFactory,
                                    ActivationSpec spec ) throws ResourceException
    {
        LOG.trace( "CuckooResourceAdapter.endpointActivation(MessageEndpointFactory, ActivationSpec)" );
        throw new NotSupportedException( "Inbound calls are not yet implemented" );
    }

    /**
     * This is called when a message endpoint is deactivated.
     *
     * @param endpointFactory a message endpoint factory instance.
     * @param spec            an activation spec JavaBean instance.
     */
    public void endpointDeactivation( MessageEndpointFactory endpointFactory,
                                      ActivationSpec spec )
    {
        LOG.trace( "CuckooResourceAdapter.endpointDeactivation(MessageEndpointFactory, ActivationSpec)" );
    }

    /**
     * This is called when a resource adapter instance is bootstrapped.
     *
     * @param ctx a bootstrap context containing references
     * @throws ResourceAdapterInternalException
     *          indicates bootstrap failure.
     */
    public void start( BootstrapContext ctx )
            throws ResourceAdapterInternalException
    {
        LOG.trace( "CuckooResourceAdapter.start(" + ctx + ")" );
        Environment.registerDestinationDataProvider( destinationDataProvider );
    }

    /**
     * This is called when a resource adapter instance is undeployed or
     * during application server shutdown.
     */
    public void stop()
    {
        LOG.trace( "CuckooResourceAdapter.stop()" );
        Environment.unregisterDestinationDataProvider( destinationDataProvider );
    }

    /**
     * This method is called by the application server during crash recovery.
     *
     * @param specs an array of ActivationSpec JavaBeans
     * @return an array of XAResource objects
     * @throws ResourceException generic exception
     */
    public XAResource[] getXAResources( ActivationSpec[] specs )
            throws ResourceException
    {
        LOG.trace( "CuckooResourceAdapter.getXAResources(ActivationSpec[])" );
        throw new NotSupportedException( "XA-Resources are not supported" );
    }

    void registerDestination( String destinationName, Properties properties ) throws ResourceException
    {
        LOG.info( "Cuckoo-RA: registering JCo destination '" + destinationName + "' with properties: " + properties );

        if ( destinationDataProvider.wasAdded( destinationName ) )
        {
            throw new ResourceException(
                    "A JCo destination with destinationName '" + destinationName + "' was already registered. "
                            +
                            "Please provide a different destinationName with each resource adapter configuration you deploy." );
        }

        destinationDataProvider.addDestination( destinationName, properties );
    }

    public CuckooRaMetaData getResourceAdapterMetaData()
    {
        LOG.trace( "CuckooResourceAdapter.getResourceAdapterMetaData()" );

        return resourceAdapterMetaData;
    }

    public CuckooRecordFactory getRecordFactory()
    {
        LOG.trace( "CuckooResourceAdapter.getRecordFactory()" );

        return recordFactory;
    }
}
