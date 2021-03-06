/*
 * Copyright (C) 2012 akquinet tech@spree GmbH
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
import org.cuckoo.ra.common.CuckooConnectionMetaData;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;
import javax.resource.spi.ManagedConnection;

public interface CuckooManagedConnection extends ManagedConnection
{
    void notifyConnectionClosed( CuckooConnection connection );

    MappedRecord executeFunction( String functionName, Record input ) throws ResourceException;

    void notifyLocalTransactionStartedEvent( Connection connectionHandle );

    void notifyLocalTransactionCommittedEvent( Connection connectionHandle );

    void notifyLocalTransactionRolledbackEvent( Connection connectionHandle );

    void beginLocalTransaction( Connection connectionHandle );

    void commitLocalTransaction( Connection connectionHandle ) throws ResourceException;

    void rollbackLocalTransaction( Connection connectionHandle ) throws ResourceException;

    void disassociateConnection( CuckooConnection connection ) throws ResourceException;

    CuckooConnectionMetaData getMetaData() throws ResourceException;
}
