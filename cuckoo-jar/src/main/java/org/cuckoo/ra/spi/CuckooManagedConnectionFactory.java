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

import org.cuckoo.ra.cci.CuckooConnectionFactory;
import org.cuckoo.ra.common.ApplicationProperties;
import org.cuckoo.ra.jco.JCoDestinationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.util.Set;

/**
 * CuckooManagedConnectionFactory
 *
 * @version $Revision: $
 */
public class CuckooManagedConnectionFactory extends ConfigurationPropertiesHolder
        implements ManagedConnectionFactory, ResourceAdapterAssociation
{
    private static final Logger LOG = LoggerFactory.getLogger( CuckooManagedConnectionFactory.class );

    private CuckooResourceAdapter resourceAdapter;

    private PrintWriter logwriter;

    private boolean initialized = false;

    public CuckooManagedConnectionFactory()
    {
        LOG.trace( "CuckooManagedConnectionFactory()" );
    }

    /**
     * Creates a Connection Factory instance.
     *
     * @param connectionManager ConnectionManager to be associated with created EIS connection factory instance
     * @return EIS-specific Connection Factory instance or javax.resource.cci.ConnectionFactory instance
     * @throws ResourceException Generic exception
     */
    public CuckooConnectionFactory createConnectionFactory( ConnectionManager connectionManager )
            throws ResourceException
    {
        LOG.trace( "CuckooManagedConnectionFactory.createConnectionFactory(ConnectionManager)" );

        if ( !initialized )
        {
            final String destinationName = getDestinationName();
            resourceAdapter.registerDestination( destinationName,
                    JCoDestinationUtil.createJCoDestinationProperties( getConfigurationProperties() ) );
            initialized = true;
        }

        return new CuckooConnectionFactory( connectionManager, this );
    }

    private void assertInitialized()
    {
        if ( !initialized )
        {
            throw new IllegalStateException( "Managed Connection Factory is not yet initialized" );
        }
    }

    /**
     * Creates a Connection Factory instance in a non-managed environment
     *
     * @return EIS-specific Connection Factory instance or javax.resource.cci.ConnectionFactory instance
     * @throws ResourceException Generic exception
     */
    public CuckooConnectionFactory createConnectionFactory() throws ResourceException
    {
        LOG.trace( "CuckooManagedConnectionFactory.createConnectionFactory()" );

        return createConnectionFactory( new CuckooConnectionManager() );
    }

    /**
     * Creates a new physical connection to the underlying EIS resource manager.
     *
     * @param subject               Caller's security information
     * @param connectionRequestInfo Additional resource adapter specific connection request information
     * @return ManagedConnection instance
     * @throws ResourceException generic exception
     */
    public ManagedConnection createManagedConnection( Subject subject,
                                                      ConnectionRequestInfo connectionRequestInfo )
            throws ResourceException
    {
        LOG.trace(
                "CuckooManagedConnectionFactory.createManagedConnection(Subject, ConnectionRequestInfo)" );

        assertInitialized();

        ApplicationProperties applicationProperties = ( ApplicationProperties ) connectionRequestInfo;

        if ( subject != null )
        {
            LOG.debug( "Container managed sign-on. Subject: " + subject );

            final Set<PasswordCredential> passwordCredentials = subject
                    .getPrivateCredentials( PasswordCredential.class );

            if ( passwordCredentials != null && !passwordCredentials.isEmpty() )
            {
                LOG.debug( "Password based sign-on based on PasswordCredentials" );

                for ( final PasswordCredential pc : passwordCredentials )
                {
                    if ( equals( pc.getManagedConnectionFactory() ) )
                    {
                        if ( applicationProperties != null )
                        {
                            LOG.debug(
                                    "Overwriting application defined configuration with user/password from Credentials" );
                            applicationProperties.setUser( pc.getUserName() );
                            applicationProperties.setPassword( String.valueOf( pc.getPassword() ) );
                        }
                        else
                        {
                            LOG.debug(
                                    "No application-defined configuration info, using configured properties and user/password from Credentials" );
                            applicationProperties =
                                    new ApplicationProperties( pc
                                            .getUserName(), String.valueOf( pc.getPassword() ) );
                        }
                    }
                }
                throw new ResourceException(
                        "No PasswordCredential found that corresponds to the ManagedConnectionFactory" );
            }

            // TODO Implement single sign-on with SAP logon ticket using org.ietf.jgss.GSSCredential; see JCA 1.5 spec chapter 9.1.5
            throw new NotSupportedException( "Only password based sign-on supported" );
        }
        else if ( applicationProperties == null )
        {
            LOG.debug(
                    "No configuration provided by application, use configuration from configuration only" );
        }
        else
        {
            LOG.debug( "Application managed sign-on. Using configuration provided by application" );
        }
        return new CuckooManagedConnection( getConfigurationProperties(), applicationProperties );
    }

    /**
     * Returns a matched connection from the candidate set of connections.
     *
     * @param connectionSet candidate connection set
     * @param subject       Caller's security information
     * @param cxRequestInfo Additional resource adapter specific connection request information
     * @return ManagedConnection if resource adapter finds an acceptable match otherwise null
     * @throws ResourceException generic exception
     */
    public ManagedConnection matchManagedConnections( Set connectionSet,
                                                      Subject subject,
                                                      ConnectionRequestInfo cxRequestInfo )
            throws ResourceException
    {
        LOG.trace(
                "CuckooManagedConnectionFactory.matchManagedConnections(Set, Subject, ConnectionRequestInfo)" );

        assertInitialized();

        if ( cxRequestInfo != null && !( cxRequestInfo instanceof ApplicationProperties ) )
        {
            LOG.warn( "ConnectionRequestInfo is not an instance of ApplicationProperties, but of " +
                    cxRequestInfo.getClass() );
            return null;
        }

        ApplicationProperties applicationProperties = ( ApplicationProperties ) cxRequestInfo;

        for ( final Object object : connectionSet )
        {
            final CuckooManagedConnection candidateConnection = ( CuckooManagedConnection ) object;

            //noinspection ConstantConditions
            if ( applicationProperties == null )
            {
                if ( candidateConnection.getApplicationProperties() == null )
                {
                    LOG.debug( "Found matching connection (no application properties): " + candidateConnection );
                    return candidateConnection;
                }
            }
            else
            {
                if ( applicationProperties.equals( candidateConnection.getApplicationProperties() ) )
                {
                    LOG.debug( "Found matching connection (equal application properties): " + candidateConnection );
                    return candidateConnection;
                }
            }
        }
        return null;
    }


    /**
     * Get the log writer for this ManagedConnectionFactory instance.
     *
     * @return PrintWriter
     * @throws ResourceException generic exception
     */
    public PrintWriter getLogWriter() throws ResourceException
    {
        LOG.trace( "CuckooManagedConnectionFactory.getLogWriter()" );
        return logwriter;
    }

    /**
     * Set the log writer for this ManagedConnectionFactory instance.
     *
     * @param out PrintWriter - an out stream for error logging and tracing
     * @throws ResourceException generic exception
     */
    public void setLogWriter( PrintWriter out ) throws ResourceException
    {
        LOG.trace( "CuckooManagedConnectionFactory.setLogWriter(PrintWriter)" );
        logwriter = out;
    }

    /**
     * Get the resource adapter
     *
     * @return The handle
     */
    public CuckooResourceAdapter getResourceAdapter()
    {
        LOG.trace( "CuckooManagedConnectionFactory.getResourceAdapter()" );
        return resourceAdapter;
    }

    /**
     * Set the resource adapter
     *
     * @param ra The handle
     */
    public void setResourceAdapter( ResourceAdapter ra ) throws ResourceException
    {
        LOG.trace( "CuckooManagedConnectionFactory.setResourceAdapter(ResourceAdapter)" );

        if ( !( ra instanceof CuckooResourceAdapter ) )
        {
            throw new IllegalArgumentException(
                    "Got instance of type " + ra.getClass().getName() + " while only type "
                            + CuckooResourceAdapter.class.getName() + " is supported." );
        }
        if ( this.resourceAdapter != null )
        {
            throw new javax.resource.spi.IllegalStateException(
                    "This method was already called, but must be called exactly once" );
        }

        this.resourceAdapter = ( CuckooResourceAdapter ) ra;
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
        if ( !super.equals( o ) )
        {
            return false;
        }

        CuckooManagedConnectionFactory that = ( CuckooManagedConnectionFactory ) o;

        return !( getConfigurationProperties() != null ?
                  !getConfigurationProperties().equals( that.getConfigurationProperties() ) :
                  that.getConfigurationProperties() != null );
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + ( getConfigurationProperties() != null ? getConfigurationProperties().hashCode() : 0 );
        return result;
    }
}
