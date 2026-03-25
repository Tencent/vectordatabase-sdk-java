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
package com.tencent.tcvectordb.service.impl.grpc;

import com.tencent.tcvectordb.model.param.collection.UploadFileParam;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.service.param.GetImageUrlParamInner;
import com.tencent.tcvectordb.service.param.QueryFileDetailsParamInner;

import java.util.Map;

/**
 * gRPC service implementation for AI document processing operations.
 * Handles file upload, document management, and AI document processing.
 * 
 * Note: All AI document service operations are not supported via gRPC and must use HTTP fallback.
 */
public class DocumentAIGrpcService extends BaseGrpcService {

    /**
     * Upload and process text files for AI document processing.
     * Note: File upload operations are not supported via gRPC and must use HTTP fallback.
     */
    public void upload(String databaseName, String collectionViewName, LoadAndSplitTextParam loadAndSplitTextParam,
                       Map<String, Object> metaDataMap) throws Exception {
        throw new UnsupportedOperationException("File upload operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Upload files to a collection for processing.
     * Note: File upload operations are not supported via gRPC and must use HTTP fallback.
     */
    public void collectionUpload(String databaseName, String collectionName, UploadFileParam loadAndSplitTextParam,
                                 Map<String, Object> metaDataMap) throws Exception {
        throw new UnsupportedOperationException("File upload operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Retrieve file information from a collection.
     * Note: File operations are not supported via gRPC and must use HTTP fallback.
     */
    public GetDocumentSetRes getFile(String databaseName, String collectionName, String documentSetName, String documentSetId) {
        throw new UnsupportedOperationException("File operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Get document chunks with pagination support.
     * Note: Document chunk operations are not supported via gRPC and must use HTTP fallback.
     */
    public GetChunksRes getChunks(String databaseName, String collectionName, String documentSetName, String documentSetId,
                                  Integer limit, Integer offset) {
        throw new UnsupportedOperationException("Document chunk operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Get image URL for document processing.
     * Note: Image operations are not supported via gRPC and must use HTTP fallback.
     */
    public GetImageUrlRes getImageUrl(GetImageUrlParamInner param) {
        throw new UnsupportedOperationException("Image operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Query detailed file information.
     * Note: File query operations are not supported via gRPC and must use HTTP fallback.
     */
    public QueryFileDetailRes queryFileDetails(QueryFileDetailsParamInner param) {
        throw new UnsupportedOperationException("File query operations not supported via gRPC, use HTTP fallback");
    }
}
