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

import org.cuckoo.ra.cci.CuckooConnection;
import org.cuckoo.ra.common.ApplicationProperties;
import org.cuckoo.ra.common.ConnectionMetaDataImpl;
import org.cuckoo.ra.jco.JCoAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CuckooManagedConnection implements ManagedConnection
{
    private static final Logger LOG = LoggerFactory.getLogger( CuckooManagedConnection.class );

    private final ConfigurationProperties configurationProperties;
    private final ApplicationProperties applicationProperties;
    private final JCoAdapter jCoAdapter;
    private final List<CuckooConnection> connectionHandlers;
    private final List<ConnectionEventListener> eventListeners;
    private final CuckooSpiLocalTransaction localTransaction;
    private final ConnectionMetaDataImpl metaData;


    CuckooManagedConnection( ConfigurationProperties configurationProperties,
                             ApplicationProperties applicationProperties ) throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.CuckooManagedConnection()" );

        this.configurationProperties = configurationProperties;
        this.applicationProperties = applicationProperties;
        this.localTransaction = new CuckooSpiLocalTransaction( this );
        connectionHandlers = Collections.synchronizedList( new ArrayList<CuckooConnection>() );
        eventListeners = Collections.synchronizedList( new ArrayList<ConnectionEventListener>() );
        jCoAdapter = new JCoAdapter( configurationProperties.getDestinationName(), applicationProperties );
        metaData = jCoAdapter.createConnectionMetaData();
    }

    ApplicationProperties getApplicationProperties()
    {
        LOG.debug( "CuckooManagedConnection.getApplicationProperties()" );

        return applicationProperties;
    }

    /**
     * Creates a new connection handle for the underlying physical connection
     * represented by the ManagedConnection instance.
     *
     * @param subject               security context as JAAS subject
     * @param connectionRequestInfo ConnectionRequestInfo instance
     * @return generic Object instance representing the connection handle.
     * @throws ResourceException generic exception if operation fails
     */
    public CuckooConnection getConnection( Subject subject,
                                              ConnectionRequestInfo connectionRequestInfo ) throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.getConnection( Subject, ConnectionRequestInfo )" );

        // TODO check subject and connectionRequestInfo are the same as in ManagedConnection

        CuckooConnection connection = new CuckooConnection( this );
        connectionHandlers.add( connection );
        return connection;
    }

    /**
     * Used by the container to change the association of an
     * application-level connection handle with a ManagedConneciton instance.
     *
     * @param connection Application-level connection handle
     * @throws ResourceException generic exception if operation fails
     */
    public void associateConnection( Object connection ) throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.associateConnection(" + connection + ")" );

        if ( connection == null )
        {
            throw new ResourceException( "Connection is null" );
        }

        if ( !( connection instanceof CuckooConnection ) )
        {
            throw new ResourceException(
                    "Only connections of type " + CuckooConnection.class.getName() + " can be handled. " +
                            "The container provided a connection of type " + connection.getClass().getName() );
        }

        final CuckooConnection connectionImpl = ( CuckooConnection ) connection;
        connectionImpl.associateManagedConnection( this );
        connectionHandlers.add( connectionImpl );
    }

    public void disassociateConnection( final CuckooConnection connection ) throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.disassociateConnection( " + connection + " )" );

        if ( !connectionHandlers.contains( connection ) )
        {
            throw new ResourceException(
                    "Connection handler is not associated with the given connection." );
        }
        connectionHandlers.remove( connection );
    }

    /**
     * Application server calls this method to force any cleanup on the ManagedConnection instance.
     *
     * @throws ResourceException generic exception if operation fails
     */
    public void cleanup() throws ResourceException

    {
        LOG.debug( "CuckooManagedConnection.cleanup()" );
        synchronized ( connectionHandlers )
        {
            for ( final CuckooConnection connectionImpl : connectionHandlers )
            {
                connectionImpl.invalidate();
            }
            connectionHandlers.clear();
        }
    }

    /**
     * Destroys the physical connection to the underlying resource manager.
     *
     * @throws ResourceException generic exception if operation fails
     */
    public void destroy() throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.destroy()" );
        synchronized ( connectionHandlers )
        {
            connectionHandlers.clear();
            jCoAdapter.disconnect();
        }
    }

    /**
     * Adds a connection event listener to the ManagedConnection instance.
     *
     * @param listener a new ConnectionEventListener to be registered
     */
    public void addConnectionEventListener( ConnectionEventListener listener )
    {
        LOG.debug( "CuckooManagedConnection.addConnectionEventListener(ConnectionEventListener)" );
        synchronized ( eventListeners )
        {
            if ( !eventListeners.contains( listener ) )
            {
                eventListeners.add( listener );
            }
        }
    }

    /**
     * Removes an already registered connection event listener from the ManagedConnection instance.
     *
     * @param listener already registered connection event listener to be removed
     */
    public void removeConnectionEventListener( ConnectionEventListener listener )
    {
        LOG.debug( "CuckooManagedConnection.removeConnectionEventListener(ConnectionEventListener)" );
        synchronized ( eventListeners )
        {
            eventListeners.remove( listener );
        }
    }

    /**
     * Gets the log writer for this ManagedConnection instance.
     *
     * @return Character ourput stream associated with this Managed-Connection instance
     * @throws ResourceException generic exception if operation fails
     */
    public PrintWriter getLogWriter() throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.getLogWriter()" );
        return null;
    }

    /**
     * Sets the log writer for this ManagedConnection instance.
     *
     * @param out Character Output stream to be associated
     * @throws ResourceException generic exception if operation fails
     */
    public void setLogWriter( PrintWriter out ) throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.setLogWriter(PrintWriter)" );
    }

    /**
     * Returns an <code>javax.resource.spi.LocalTransaction</code> instance.
     *
     * @return LocalTransaction instance
     */
    public CuckooSpiLocalTransaction getLocalTransaction() 
    {
        LOG.debug( "CuckooManagedConnection.getLocalTransaction()" );
        return localTransaction;
    }

    /**
     * Returns an <code>javax.transaction.xa.XAresource</code> instance.
     *
     * @return XAResource instance
     * @throws ResourceException generic exception if operation fails
     */
    public XAResource getXAResource() throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.getXAResource()" );
        throw new NotSupportedException( "XA is not supported, since SAP does not implement 2-phase-commit" );
    }

    /**
     * Gets the metadata information for this connection's underlying EIS resource manager instance.
     *
     * @return ManagedConnectionMetaData instance
     * @throws ResourceException generic exception if operation fails
     */
    public ConnectionMetaDataImpl getMetaData() throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.getMetaData()" );
        return metaData;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        CuckooManagedConnection that = ( CuckooManagedConnection ) o;

        if ( applicationProperties != null ? !applicationProperties.equals( that.applicationProperties ) :
             that.applicationProperties != null )
        {
            return false;
        }
        //noinspection RedundantIfStatement
        if ( configurationProperties != null ? !configurationProperties.equals( that.configurationProperties ) :
             that.configurationProperties != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = configurationProperties != null ? configurationProperties.hashCode() : 0;
        result = 31 * result + ( applicationProperties != null ? applicationProperties.hashCode() : 0 );
        return result;
    }

    public void notifyConnectionClosed( CuckooConnection connection )
    {
        LOG.debug( "CuckooManagedConnection.notifyConnectionClosed()" );

        final ConnectionEvent event = new ConnectionEvent( this, ConnectionEvent.CONNECTION_CLOSED );
        event.setConnectionHandle( connection );

        synchronized ( eventListeners )
        {
            for ( final ConnectionEventListener listener : eventListeners )
            {
                listener.connectionClosed( event );
            }
        }
    }

    public MappedRecord executeFunction( String functionName, Record input ) throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.executeFunction()" );

        return jCoAdapter.executeFunction( functionName, input );
    }

    public void startTransaction()
    {
        LOG.debug( "CuckooManagedConnection.startTransaction()" );

        jCoAdapter.startTransaction();
    }

    public void commitTransaction() throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.commitTransaction()" );

        jCoAdapter.commitTransaction();
    }

    public void rollbackTransaction() throws ResourceException
    {
        LOG.debug( "CuckooManagedConnection.rollbackTransaction()" );

        jCoAdapter.rollbackTransaction();
    }

    public void notifyLocalTransactionStartedEvent( Connection connectionHandle )
    {
        final ConnectionEvent event = new ConnectionEvent( this, ConnectionEvent.LOCAL_TRANSACTION_STARTED );
        event.setConnectionHandle( connectionHandle );

        synchronized ( eventListeners )
        {
            for ( ConnectionEventListener eventListener : eventListeners )
            {
                eventListener.localTransactionStarted( event );
            }
        }

    }

    public void notifyLocalTransactionCommittedEvent( Connection connectionHandle )
    {
        final ConnectionEvent event = new ConnectionEvent( this, ConnectionEvent.LOCAL_TRANSACTION_COMMITTED );
        event.setConnectionHandle( connectionHandle );

        synchronized ( eventListeners )
        {
            for ( ConnectionEventListener eventListener : eventListeners )
            {
                eventListener.localTransactionCommitted( event );
            }
        }
    }

    public void notifyLocalTransactionRolledbackEvent( Connection connectionHandle )
    {
        final ConnectionEvent event = new ConnectionEvent( this, ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK );
        event.setConnectionHandle( connectionHandle );

        synchronized ( eventListeners )
        {
            for ( ConnectionEventListener eventListener : eventListeners )
            {
                eventListener.localTransactionRolledback( event );
            }
        }

    }
}
