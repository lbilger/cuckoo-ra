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
package org.cuckoo.ra.cci;

import org.junit.Test;

import static org.cuckoo.ra.util.CuckooTestUtil.serializeAndReturnDeserializedObject;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

@SuppressWarnings( {"unchecked"} )
public class CuckooMappedRecordTest
{
    @Test
    public void isSerializable() throws Exception
    {
        CuckooMappedRecord record = new CuckooMappedRecord( "recordName" );
        record.setRecordShortDescription( "recordShortDescription" );
        record.put( "key", "value" );
        CuckooMappedRecord innerRecord = new CuckooMappedRecord( "innerRecordName" );
        innerRecord.put( "key2", "value2" );
        record.put( "keyInnerRecord", innerRecord );

        CuckooMappedRecord deserializedRecord = serializeAndReturnDeserializedObject( record );

        assertThat( deserializedRecord, is( not( sameInstance( record ) ) ) );

        assertThat( deserializedRecord.getRecordName(), equalTo( "recordName" ) );
        assertThat( deserializedRecord.getRecordShortDescription(), equalTo( "recordShortDescription" ) );
        assertThat( deserializedRecord.size(), is( 2 ) );

        CuckooMappedRecord deserializedInnerRecord = ( CuckooMappedRecord ) deserializedRecord.get( "keyInnerRecord" );

        assertThat( deserializedInnerRecord, is( not( sameInstance( innerRecord ) ) ) );
        assertThat( deserializedInnerRecord.size(), is( 1 ) );
        assertThat( deserializedInnerRecord.getRecordName(), equalTo( "innerRecordName" ) );
        assertThat( deserializedInnerRecord.getRecordShortDescription(), is( nullValue() ) );
        assertThat( ( String ) deserializedInnerRecord.get( "key2" ), equalTo( "value2" ) );
    }

    @Test
    public void isCloneable() throws Exception
    {
        CuckooMappedRecord record = new CuckooMappedRecord( "recordName" );
        record.setRecordShortDescription( "recordShortDescription" );
        record.put( "key", "value" );
        CuckooMappedRecord innerRecord = new CuckooMappedRecord( "innerRecordName" );
        innerRecord.put( "key2", "value2" );
        record.put( "keyInnerRecord", innerRecord );

        CuckooMappedRecord clone = record.clone();

        assertThat( clone, is( not( sameInstance( record ) ) ) );

        assertThat( clone.getRecordName(), equalTo( "recordName" ) );
        assertThat( clone.getRecordShortDescription(), equalTo( "recordShortDescription" ) );
        assertThat( clone.size(), is( 2 ) );
        assertThat( ( String ) clone.get( "key" ), equalTo( "value" ) );

        CuckooMappedRecord innerRecordClone = ( CuckooMappedRecord ) clone.get( "keyInnerRecord" );

        assertThat( innerRecordClone.size(), is( 1 ) );
        assertThat( innerRecordClone.getRecordName(), equalTo( "innerRecordName" ) );
        assertThat( innerRecordClone.getRecordShortDescription(), is( nullValue() ) );
        assertThat( ( String ) innerRecordClone.get( "key2" ), equalTo( "value2" ) );
    }
}
