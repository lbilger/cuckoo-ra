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
package org.cuckoo.ra.cci;

import org.cuckoo.ra.common.ApplicationProperties;
import org.cuckoo.ra.spi.CuckooManagedConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import javax.resource.spi.ConnectionManager;


public class CuckooConnectionFactory implements ConnectionFactory
{
    private static final Logger LOG = LoggerFactory.getLogger( CuckooConnectionFactory.class);

    private Reference reference;
    private ConnectionManager connectionManager;
    private CuckooManagedConnectionFactory managedConnectionFactory;

    /**
     * @param connectionManager        ConnectionManager
     * @param managedConnectionFactory The ManagedConnectionFactory instance.
     */
    public CuckooConnectionFactory(ConnectionManager connectionManager, CuckooManagedConnectionFactory managedConnectionFactory)
    {
        LOG.trace("CuckooConnectionFactory.CuckooConnectionFactory( ConnectionManager )");
        this.connectionManager = connectionManager;
        this.managedConnectionFactory = managedConnectionFactory;
    }

    /**
     * Gets a connection to an EIS instance.
     *
     * @return Connection instance the EIS instance.
     * @throws ResourceException Failed to get a connection to
     */
    public CuckooConnection getConnection() throws ResourceException
    {
        LOG.trace("CuckooConnectionFactory.getConnection()");
        return ( CuckooConnection ) connectionManager.allocateConnection(managedConnectionFactory, null);
    }

    /**
     * Gets a connection to an EIS instance.
     *
     * @param connSpec Connection parameters and security information specified as ConnectionSpec instance
     * @return Connection instance the EIS instance.
     * @throws ResourceException Failed to get a connection to
     */
    public CuckooConnection getConnection(ConnectionSpec connSpec) throws ResourceException
    {
        LOG.trace("CuckooConnectionFactory.getConnection( ConnectionSpec )");
        if (connSpec == null)
        {
            throw new IllegalArgumentException("The ConnectionSpec passed as an argument must not be null");
        }
        if (!(connSpec instanceof ApplicationProperties))
        {
            throw new IllegalArgumentException(
                    "The ConnectionSpec passed as an argument must be of type " + ApplicationProperties.class.getName());
        }
        final ApplicationProperties specImpl = (ApplicationProperties) connSpec;
        return ( CuckooConnection ) connectionManager.allocateConnection(managedConnectionFactory, new ApplicationProperties(specImpl));
    }

    /**
     * Gets metadata for the Resource Adapter.
     *
     * @return ResourceAdapterMetaData instance
     * @throws ResourceException Failed to get metadata information
     */
    public CuckooRaMetaData getMetaData() throws ResourceException
    {
        LOG.trace("CuckooConnectionFactory.getMetaData()");
        return managedConnectionFactory.getResourceAdapter().getResourceAdapterMetaData();
    }

    /**
     * Gets a RecordFactory instance.
     *
     * @return RecordFactory instance
     * @throws ResourceException Failed to create a RecordFactory
     * @throws javax.resource.NotSupportedException
     *                           Operation not supported
     */
    public CuckooRecordFactory getRecordFactory() throws ResourceException
    {
        LOG.trace("CuckooConnectionFactory.getRecordFactory()");
        return managedConnectionFactory.getResourceAdapter().getRecordFactory();
    }

    /**
     * Get the Reference instance.
     *
     * @return Reference instance
     */
    public Reference getReference() throws NamingException
    {
        LOG.trace("CuckooConnectionFactory.getReference()");
        return reference;
    }

    /**
     * Set the Reference instance.
     *
     * @param reference A Reference instance
     */
    public void setReference(Reference reference)
    {
        LOG.trace("CuckooConnectionFactory.setReference( Reference )");

        this.reference = reference;
    }
}
