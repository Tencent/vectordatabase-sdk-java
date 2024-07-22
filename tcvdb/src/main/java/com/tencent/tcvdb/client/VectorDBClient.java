/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.tcvdb.client;

import com.tencent.tcvdb.exception.VectorDBException;
import com.tencent.tcvdb.model.param.database.ConnectParam;
import com.tencent.tcvdb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvdb.service.HttpStub;
import com.tencent.tcvdb.service.Stub;
import com.tencent.tcvdb.model.Database;

import java.util.List;

/**
 * VectorDB Client
 */
public class VectorDBClient {

    private final Stub stub;
    private final ReadConsistencyEnum readConsistency;

    public VectorDBClient(ConnectParam connectParam, ReadConsistencyEnum readConsistency) {
        this.stub = new HttpStub(connectParam);
        this.readConsistency = readConsistency;
    }

    public Database createDatabase(String databaseName) throws VectorDBException {
        Database db = database(databaseName, readConsistency);
        stub.createDatabase(db);
        return db;
    }

    public Database dropDatabase(String databaseName) throws VectorDBException {
        Database db = database(databaseName, readConsistency);
        stub.dropDatabase(db);
        return db;
    }

    public List<String> listDatabase() throws VectorDBException {
        return stub.listDatabases();
    }

    /**
     * this method is deprecated, recommend use {@link VectorDBClient#database(String)}
     *
     * @param databaseName
     * @param readConsistency
     * @return
     */
    @Deprecated
    public Database database(String databaseName, ReadConsistencyEnum readConsistency) {
        return new Database(this.stub, databaseName, readConsistency);
    }

    public Database database(String databaseName) {
        return new Database(this.stub, databaseName, this.readConsistency);
    }

}
