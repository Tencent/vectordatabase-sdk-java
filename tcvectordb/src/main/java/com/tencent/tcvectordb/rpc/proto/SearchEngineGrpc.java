package com.tencent.tcvectordb.rpc.proto;

import com.tencent.tcvectordb.service.ApiPath;
import org.checkerframework.checker.units.qual.A;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler",
    comments = "Source: olama.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class  SearchEngineGrpc {

  private SearchEngineGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.tencent.tcvectordb.rpc.proto.SearchEngine";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> getSetAliasMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.SET_COL_ALIAS,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> getSetAliasMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest, com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> getSetAliasMethod;
    if ((getSetAliasMethod = SearchEngineGrpc.getSetAliasMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getSetAliasMethod = SearchEngineGrpc.getSetAliasMethod) == null) {
          SearchEngineGrpc.getSetAliasMethod = getSetAliasMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest, com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.SET_COL_ALIAS)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier("setAlias"))
              .build();
        }
      }
    }
    return getSetAliasMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse> getGetAliasMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAlias",
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse> getGetAliasMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest, com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse> getGetAliasMethod;
    if ((getGetAliasMethod = SearchEngineGrpc.getGetAliasMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getGetAliasMethod = SearchEngineGrpc.getGetAliasMethod) == null) {
          SearchEngineGrpc.getGetAliasMethod = getGetAliasMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest, com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAlias"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier("getAlias"))
              .build();
        }
      }
    }
    return getGetAliasMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> getDeleteAliasMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DELETE_COL_ALIAS,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> getDeleteAliasMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest, com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> getDeleteAliasMethod;
    if ((getDeleteAliasMethod = SearchEngineGrpc.getDeleteAliasMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getDeleteAliasMethod = SearchEngineGrpc.getDeleteAliasMethod) == null) {
          SearchEngineGrpc.getDeleteAliasMethod = getDeleteAliasMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest, com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DELETE_COL_ALIAS)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DELETE_COL_ALIAS))
              .build();
        }
      }
    }
    return getDeleteAliasMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse> getCreateCollectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.COL_CREATE,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse> getCreateCollectionMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest, com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse> getCreateCollectionMethod;
    if ((getCreateCollectionMethod = SearchEngineGrpc.getCreateCollectionMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getCreateCollectionMethod = SearchEngineGrpc.getCreateCollectionMethod) == null) {
          SearchEngineGrpc.getCreateCollectionMethod = getCreateCollectionMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest, com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.COL_CREATE)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.COL_CREATE))
              .build();
        }
      }
    }
    return getCreateCollectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse> getDropCollectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.COL_DROP,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse> getDropCollectionMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest, com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse> getDropCollectionMethod;
    if ((getDropCollectionMethod = SearchEngineGrpc.getDropCollectionMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getDropCollectionMethod = SearchEngineGrpc.getDropCollectionMethod) == null) {
          SearchEngineGrpc.getDropCollectionMethod = getDropCollectionMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest, com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.COL_DROP)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.COL_DROP))
              .build();
        }
      }
    }
    return getDropCollectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse> getTruncateCollectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.COL_FLUSH,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse> getTruncateCollectionMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest, com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse> getTruncateCollectionMethod;
    if ((getTruncateCollectionMethod = SearchEngineGrpc.getTruncateCollectionMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getTruncateCollectionMethod = SearchEngineGrpc.getTruncateCollectionMethod) == null) {
          SearchEngineGrpc.getTruncateCollectionMethod = getTruncateCollectionMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest, com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.COL_FLUSH)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.COL_FLUSH))
              .build();
        }
      }
    }
    return getTruncateCollectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse> getDescribeCollectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.COL_DESCRIBE,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse> getDescribeCollectionMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest, com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse> getDescribeCollectionMethod;
    if ((getDescribeCollectionMethod = SearchEngineGrpc.getDescribeCollectionMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getDescribeCollectionMethod = SearchEngineGrpc.getDescribeCollectionMethod) == null) {
          SearchEngineGrpc.getDescribeCollectionMethod = getDescribeCollectionMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest, com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.COL_DESCRIBE)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.COL_DESCRIBE))
              .build();
        }
      }
    }
    return getDescribeCollectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse> getListCollectionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.COL_LIST,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse> getListCollectionsMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest, com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse> getListCollectionsMethod;
    if ((getListCollectionsMethod = SearchEngineGrpc.getListCollectionsMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getListCollectionsMethod = SearchEngineGrpc.getListCollectionsMethod) == null) {
          SearchEngineGrpc.getListCollectionsMethod = getListCollectionsMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest, com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.COL_LIST)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.COL_LIST))
              .build();
        }
      }
    }
    return getListCollectionsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse> getRebuildIndexMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.REBUILD_INDEX,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse> getRebuildIndexMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest, com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse> getRebuildIndexMethod;
    if ((getRebuildIndexMethod = SearchEngineGrpc.getRebuildIndexMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getRebuildIndexMethod = SearchEngineGrpc.getRebuildIndexMethod) == null) {
          SearchEngineGrpc.getRebuildIndexMethod = getRebuildIndexMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest, com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.REBUILD_INDEX)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.REBUILD_INDEX))
              .build();
        }
      }
    }
    return getRebuildIndexMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse> getUpsertMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DOC_UPSERT,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse> getUpsertMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest, com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse> getUpsertMethod;
    if ((getUpsertMethod = SearchEngineGrpc.getUpsertMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getUpsertMethod = SearchEngineGrpc.getUpsertMethod) == null) {
          SearchEngineGrpc.getUpsertMethod = getUpsertMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest, com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DOC_UPSERT)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DOC_UPSERT))
              .build();
        }
      }
    }
    return getUpsertMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse> getUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DOC_UPDATE,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse> getUpdateMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest, com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse> getUpdateMethod;
    if ((getUpdateMethod = SearchEngineGrpc.getUpdateMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getUpdateMethod = SearchEngineGrpc.getUpdateMethod) == null) {
          SearchEngineGrpc.getUpdateMethod = getUpdateMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest, com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DOC_UPDATE)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DOC_UPDATE))
              .build();
        }
      }
    }
    return getUpdateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse> getQueryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DOC_QUERY,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse> getQueryMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest, com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse> getQueryMethod;
    if ((getQueryMethod = SearchEngineGrpc.getQueryMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getQueryMethod = SearchEngineGrpc.getQueryMethod) == null) {
          SearchEngineGrpc.getQueryMethod = getQueryMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest, com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DOC_QUERY)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DOC_QUERY))
              .build();
        }
      }
    }
    return getQueryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> getSearchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DOC_SEARCH,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> getSearchMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest, com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> getSearchMethod;
    if ((getSearchMethod = SearchEngineGrpc.getSearchMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getSearchMethod = SearchEngineGrpc.getSearchMethod) == null) {
          SearchEngineGrpc.getSearchMethod = getSearchMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest, com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DOC_SEARCH)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DOC_SEARCH))
              .build();
        }
      }
    }
    return getSearchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> getHybridSearchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DOC_HYBRID_SEARCH,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> getHybridSearchMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest, com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> getHybridSearchMethod;
    if ((getHybridSearchMethod = SearchEngineGrpc.getHybridSearchMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getHybridSearchMethod = SearchEngineGrpc.getHybridSearchMethod) == null) {
          SearchEngineGrpc.getHybridSearchMethod = getHybridSearchMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest, com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DOC_HYBRID_SEARCH)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DOC_HYBRID_SEARCH))
              .build();
        }
      }
    }
    return getHybridSearchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest> getKeywordSearchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "keyword_search",
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest> getKeywordSearchMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest, com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest> getKeywordSearchMethod;
    if ((getKeywordSearchMethod = SearchEngineGrpc.getKeywordSearchMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getKeywordSearchMethod = SearchEngineGrpc.getKeywordSearchMethod) == null) {
          SearchEngineGrpc.getKeywordSearchMethod = getKeywordSearchMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest, com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "keyword_search"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier("keyword_search"))
              .build();
        }
      }
    }
    return getKeywordSearchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse> getExplainMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DOC_COUNT,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse> getExplainMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest, com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse> getExplainMethod;
    if ((getExplainMethod = SearchEngineGrpc.getExplainMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getExplainMethod = SearchEngineGrpc.getExplainMethod) == null) {
          SearchEngineGrpc.getExplainMethod = getExplainMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest, com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DOC_COUNT)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DOC_COUNT))
              .build();
        }
      }
    }
    return getExplainMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse> getDeleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DOC_DELETE,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse> getDeleMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest, com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse> getDeleMethod;
    if ((getDeleMethod = SearchEngineGrpc.getDeleMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getDeleMethod = SearchEngineGrpc.getDeleMethod) == null) {
          SearchEngineGrpc.getDeleMethod = getDeleMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest, com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DOC_DELETE)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DOC_DELETE))
              .build();
        }
      }
    }
    return getDeleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> getRangeSearchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "range_search",
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> getRangeSearchMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest, com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> getRangeSearchMethod;
    if ((getRangeSearchMethod = SearchEngineGrpc.getRangeSearchMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getRangeSearchMethod = SearchEngineGrpc.getRangeSearchMethod) == null) {
          SearchEngineGrpc.getRangeSearchMethod = getRangeSearchMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest, com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "range_search"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier("range_search"))
              .build();
        }
      }
    }
    return getRangeSearchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SortRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SortResponse> getSortMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sort",
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.SortRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.SortResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SortRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.SortResponse> getSortMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.SortRequest, com.tencent.tcvectordb.rpc.proto.Olama.SortResponse> getSortMethod;
    if ((getSortMethod = SearchEngineGrpc.getSortMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getSortMethod = SearchEngineGrpc.getSortMethod) == null) {
          SearchEngineGrpc.getSortMethod = getSortMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.SortRequest, com.tencent.tcvectordb.rpc.proto.Olama.SortResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sort"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SortRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.SortResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier("sort"))
              .build();
        }
      }
    }
    return getSortMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> getCreateDatabaseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DB_CREATE,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> getCreateDatabaseMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest, com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> getCreateDatabaseMethod;
    if ((getCreateDatabaseMethod = SearchEngineGrpc.getCreateDatabaseMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getCreateDatabaseMethod = SearchEngineGrpc.getCreateDatabaseMethod) == null) {
          SearchEngineGrpc.getCreateDatabaseMethod = getCreateDatabaseMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest, com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DB_CREATE)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DB_CREATE))
              .build();
        }
      }
    }
    return getCreateDatabaseMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> getDropDatabaseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DB_DROP,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> getDropDatabaseMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest, com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> getDropDatabaseMethod;
    if ((getDropDatabaseMethod = SearchEngineGrpc.getDropDatabaseMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getDropDatabaseMethod = SearchEngineGrpc.getDropDatabaseMethod) == null) {
          SearchEngineGrpc.getDropDatabaseMethod = getDropDatabaseMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest, com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DB_DROP)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DB_DROP))
              .build();
        }
      }
    }
    return getDropDatabaseMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> getListDatabasesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DB_LIST,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> getListDatabasesMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest, com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> getListDatabasesMethod;
    if ((getListDatabasesMethod = SearchEngineGrpc.getListDatabasesMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getListDatabasesMethod = SearchEngineGrpc.getListDatabasesMethod) == null) {
          SearchEngineGrpc.getListDatabasesMethod = getListDatabasesMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest, com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DB_LIST)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DB_LIST))
              .build();
        }
      }
    }
    return getListDatabasesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse> getDescribeDatabaseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.DB_DESCRIBE,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse> getDescribeDatabaseMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest, com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse> getDescribeDatabaseMethod;
    if ((getDescribeDatabaseMethod = SearchEngineGrpc.getDescribeDatabaseMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getDescribeDatabaseMethod = SearchEngineGrpc.getDescribeDatabaseMethod) == null) {
          SearchEngineGrpc.getDescribeDatabaseMethod = getDescribeDatabaseMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest, com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.DB_DESCRIBE)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.DB_DESCRIBE))
              .build();
        }
      }
    }
    return getDescribeDatabaseMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse> getGetVersionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "get_version",
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse> getGetVersionMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest, com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse> getGetVersionMethod;
    if ((getGetVersionMethod = SearchEngineGrpc.getGetVersionMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getGetVersionMethod = SearchEngineGrpc.getGetVersionMethod) == null) {
          SearchEngineGrpc.getGetVersionMethod = getGetVersionMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest, com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "get_version"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier("get_version"))
              .build();
        }
      }
    }
    return getGetVersionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse> getAddIndexMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = ApiPath.ADD_INDEX,
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse> getAddIndexMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest, com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse> getAddIndexMethod;
    if ((getAddIndexMethod = SearchEngineGrpc.getAddIndexMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getAddIndexMethod = SearchEngineGrpc.getAddIndexMethod) == null) {
          SearchEngineGrpc.getAddIndexMethod = getAddIndexMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest, com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(ApiPath.ADD_INDEX)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier(ApiPath.ADD_INDEX))
              .build();
        }
      }
    }
    return getAddIndexMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse> getModifyVectorIndexMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "modifyVectorIndex",
      requestType = com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest.class,
      responseType = com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest,
      com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse> getModifyVectorIndexMethod() {
    io.grpc.MethodDescriptor<com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest, com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse> getModifyVectorIndexMethod;
    if ((getModifyVectorIndexMethod = SearchEngineGrpc.getModifyVectorIndexMethod) == null) {
      synchronized (SearchEngineGrpc.class) {
        if ((getModifyVectorIndexMethod = SearchEngineGrpc.getModifyVectorIndexMethod) == null) {
          SearchEngineGrpc.getModifyVectorIndexMethod = getModifyVectorIndexMethod =
              io.grpc.MethodDescriptor.<com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest, com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "modifyVectorIndex"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SearchEngineMethodDescriptorSupplier("modifyVectorIndex"))
              .build();
        }
      }
    }
    return getModifyVectorIndexMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SearchEngineStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SearchEngineStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SearchEngineStub>() {
        @java.lang.Override
        public SearchEngineStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SearchEngineStub(channel, callOptions);
        }
      };
    return SearchEngineStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SearchEngineBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SearchEngineBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SearchEngineBlockingStub>() {
        @java.lang.Override
        public SearchEngineBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SearchEngineBlockingStub(channel, callOptions);
        }
      };
    return SearchEngineBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SearchEngineFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SearchEngineFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SearchEngineFutureStub>() {
        @java.lang.Override
        public SearchEngineFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SearchEngineFutureStub(channel, callOptions);
        }
      };
    return SearchEngineFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * 修改别名指向
     * </pre>
     */
    default void setAlias(com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetAliasMethod(), responseObserver);
    }

    /**
     * <pre>
     * 查询别名指向
     * </pre>
     */
    default void getAlias(com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAliasMethod(), responseObserver);
    }

    /**
     * <pre>
     * 删除别名指向
     * </pre>
     */
    default void deleteAlias(com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteAliasMethod(), responseObserver);
    }

    /**
     * <pre>
     * 创建索引
     * </pre>
     */
    default void createCollection(com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateCollectionMethod(), responseObserver);
    }

    /**
     * <pre>
     * 删除索引
     * </pre>
     */
    default void dropCollection(com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDropCollectionMethod(), responseObserver);
    }

    /**
     * <pre>
     * 清空索引
     * </pre>
     */
    default void truncateCollection(com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTruncateCollectionMethod(), responseObserver);
    }

    /**
     * <pre>
     * 显示索引配置
     * </pre>
     */
    default void describeCollection(com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDescribeCollectionMethod(), responseObserver);
    }

    /**
     * <pre>
     * 显示全部索引
     * </pre>
     */
    default void listCollections(com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListCollectionsMethod(), responseObserver);
    }

    /**
     * <pre>
     * 重建索引
     * </pre>
     */
    default void rebuildIndex(com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRebuildIndexMethod(), responseObserver);
    }

    /**
     * <pre>
     * 写入向量
     * </pre>
     */
    default void upsert(com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpsertMethod(), responseObserver);
    }

    /**
     * <pre>
     * 更新向量
     * </pre>
     */
    default void update(com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateMethod(), responseObserver);
    }

    /**
     * <pre>
     * 查询向量
     * </pre>
     */
    default void query(com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQueryMethod(), responseObserver);
    }

    /**
     * <pre>
     * KNN搜索
     * </pre>
     */
    default void search(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchMethod(), responseObserver);
    }

    /**
     * <pre>
     * 混合搜索
     * </pre>
     */
    default void hybridSearch(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHybridSearchMethod(), responseObserver);
    }

    /**
     * <pre>
     * 关键词检索
     * </pre>
     */
    default void keywordSearch(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getKeywordSearchMethod(), responseObserver);
    }

    /**
     * <pre>
     * explain
     * </pre>
     */
    default void explain(com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExplainMethod(), responseObserver);
    }

    /**
     * <pre>
     * 删除向量
     * </pre>
     */
    default void dele(com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleMethod(), responseObserver);
    }

    /**
     * <pre>
     * 范围搜索
     * </pre>
     */
    default void rangeSearch(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRangeSearchMethod(), responseObserver);
    }

    /**
     * <pre>
     * 指定候选集排序
     * </pre>
     */
    default void sort(com.tencent.tcvectordb.rpc.proto.Olama.SortRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SortResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSortMethod(), responseObserver);
    }

    /**
     * <pre>
     * 创建 database
     * </pre>
     */
    default void createDatabase(com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateDatabaseMethod(), responseObserver);
    }

    /**
     * <pre>
     * 删除 database
     * </pre>
     */
    default void dropDatabase(com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDropDatabaseMethod(), responseObserver);
    }

    /**
     * <pre>
     * 显示全部 database
     * </pre>
     */
    default void listDatabases(com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListDatabasesMethod(), responseObserver);
    }

    /**
     * <pre>
     * 显示数据库
     * </pre>
     */
    default void describeDatabase(com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDescribeDatabaseMethod(), responseObserver);
    }

    /**
     * <pre>
     * 获取版本（api升级兼容性考虑）
     * </pre>
     */
    default void getVersion(com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetVersionMethod(), responseObserver);
    }

    /**
     * <pre>
     * 新增scalar的索引
     * </pre>
     */
    default void addIndex(com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddIndexMethod(), responseObserver);
    }

    /**
     * <pre>
     * 修改index配置
     * </pre>
     */
    default void modifyVectorIndex(com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getModifyVectorIndexMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SearchEngine.
   */
  public static abstract class SearchEngineImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SearchEngineGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SearchEngine.
   */
  public static final class SearchEngineStub
      extends io.grpc.stub.AbstractAsyncStub<SearchEngineStub> {
    private SearchEngineStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SearchEngineStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SearchEngineStub(channel, callOptions);
    }

    /**
     * <pre>
     * 修改别名指向
     * </pre>
     */
    public void setAlias(com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetAliasMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 查询别名指向
     * </pre>
     */
    public void getAlias(com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAliasMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 删除别名指向
     * </pre>
     */
    public void deleteAlias(com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteAliasMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 创建索引
     * </pre>
     */
    public void createCollection(com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateCollectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 删除索引
     * </pre>
     */
    public void dropCollection(com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDropCollectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 清空索引
     * </pre>
     */
    public void truncateCollection(com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTruncateCollectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 显示索引配置
     * </pre>
     */
    public void describeCollection(com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDescribeCollectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 显示全部索引
     * </pre>
     */
    public void listCollections(com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListCollectionsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 重建索引
     * </pre>
     */
    public void rebuildIndex(com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRebuildIndexMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 写入向量
     * </pre>
     */
    public void upsert(com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpsertMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 更新向量
     * </pre>
     */
    public void update(com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 查询向量
     * </pre>
     */
    public void query(com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQueryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * KNN搜索
     * </pre>
     */
    public void search(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 混合搜索
     * </pre>
     */
    public void hybridSearch(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHybridSearchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 关键词检索
     * </pre>
     */
    public void keywordSearch(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getKeywordSearchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * explain
     * </pre>
     */
    public void explain(com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExplainMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 删除向量
     * </pre>
     */
    public void dele(com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 范围搜索
     * </pre>
     */
    public void rangeSearch(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRangeSearchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 指定候选集排序
     * </pre>
     */
    public void sort(com.tencent.tcvectordb.rpc.proto.Olama.SortRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SortResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSortMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 创建 database
     * </pre>
     */
    public void createDatabase(com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateDatabaseMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 删除 database
     * </pre>
     */
    public void dropDatabase(com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDropDatabaseMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 显示全部 database
     * </pre>
     */
    public void listDatabases(com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListDatabasesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 显示数据库
     * </pre>
     */
    public void describeDatabase(com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDescribeDatabaseMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 获取版本（api升级兼容性考虑）
     * </pre>
     */
    public void getVersion(com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetVersionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 新增scalar的索引
     * </pre>
     */
    public void addIndex(com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddIndexMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 修改index配置
     * </pre>
     */
    public void modifyVectorIndex(com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest request,
        io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getModifyVectorIndexMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SearchEngine.
   */
  public static final class SearchEngineBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SearchEngineBlockingStub> {
    private SearchEngineBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SearchEngineBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SearchEngineBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 修改别名指向
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse setAlias(com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetAliasMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 查询别名指向
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse getAlias(com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAliasMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 删除别名指向
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse deleteAlias(com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteAliasMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 创建索引
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse createCollection(com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateCollectionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 删除索引
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse dropCollection(com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDropCollectionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 清空索引
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse truncateCollection(com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTruncateCollectionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 显示索引配置
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse describeCollection(com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDescribeCollectionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 显示全部索引
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse listCollections(com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListCollectionsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 重建索引
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse rebuildIndex(com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRebuildIndexMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 写入向量
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse upsert(com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpsertMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 更新向量
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse update(com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 查询向量
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse query(com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQueryMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * KNN搜索
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse search(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 混合搜索
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse hybridSearch(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHybridSearchMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 关键词检索
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest keywordSearch(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getKeywordSearchMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * explain
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse explain(com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExplainMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 删除向量
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse dele(com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 范围搜索
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse rangeSearch(com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRangeSearchMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 指定候选集排序
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.SortResponse sort(com.tencent.tcvectordb.rpc.proto.Olama.SortRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSortMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 创建 database
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse createDatabase(com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateDatabaseMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 删除 database
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse dropDatabase(com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDropDatabaseMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 显示全部 database
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse listDatabases(com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListDatabasesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 显示数据库
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse describeDatabase(com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDescribeDatabaseMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 获取版本（api升级兼容性考虑）
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse getVersion(com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetVersionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 新增scalar的索引
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse addIndex(com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddIndexMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 修改index配置
     * </pre>
     */
    public com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse modifyVectorIndex(com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getModifyVectorIndexMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SearchEngine.
   */
  public static final class SearchEngineFutureStub
      extends io.grpc.stub.AbstractFutureStub<SearchEngineFutureStub> {
    private SearchEngineFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SearchEngineFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SearchEngineFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 修改别名指向
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> setAlias(
        com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetAliasMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 查询别名指向
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse> getAlias(
        com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAliasMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 删除别名指向
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse> deleteAlias(
        com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteAliasMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 创建索引
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse> createCollection(
        com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateCollectionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 删除索引
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse> dropCollection(
        com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDropCollectionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 清空索引
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse> truncateCollection(
        com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTruncateCollectionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 显示索引配置
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse> describeCollection(
        com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDescribeCollectionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 显示全部索引
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse> listCollections(
        com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListCollectionsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 重建索引
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse> rebuildIndex(
        com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRebuildIndexMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 写入向量
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse> upsert(
        com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpsertMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 更新向量
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse> update(
        com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 查询向量
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse> query(
        com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQueryMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * KNN搜索
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> search(
        com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 混合搜索
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> hybridSearch(
        com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHybridSearchMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 关键词检索
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest> keywordSearch(
        com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getKeywordSearchMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * explain
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse> explain(
        com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExplainMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 删除向量
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse> dele(
        com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 范围搜索
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse> rangeSearch(
        com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRangeSearchMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 指定候选集排序
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.SortResponse> sort(
        com.tencent.tcvectordb.rpc.proto.Olama.SortRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSortMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 创建 database
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> createDatabase(
        com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateDatabaseMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 删除 database
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> dropDatabase(
        com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDropDatabaseMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 显示全部 database
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse> listDatabases(
        com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListDatabasesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 显示数据库
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse> describeDatabase(
        com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDescribeDatabaseMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 获取版本（api升级兼容性考虑）
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse> getVersion(
        com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetVersionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 新增scalar的索引
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse> addIndex(
        com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddIndexMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 修改index配置
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse> modifyVectorIndex(
        com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getModifyVectorIndexMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SET_ALIAS = 0;
  private static final int METHODID_GET_ALIAS = 1;
  private static final int METHODID_DELETE_ALIAS = 2;
  private static final int METHODID_CREATE_COLLECTION = 3;
  private static final int METHODID_DROP_COLLECTION = 4;
  private static final int METHODID_TRUNCATE_COLLECTION = 5;
  private static final int METHODID_DESCRIBE_COLLECTION = 6;
  private static final int METHODID_LIST_COLLECTIONS = 7;
  private static final int METHODID_REBUILD_INDEX = 8;
  private static final int METHODID_UPSERT = 9;
  private static final int METHODID_UPDATE = 10;
  private static final int METHODID_QUERY = 11;
  private static final int METHODID_SEARCH = 12;
  private static final int METHODID_HYBRID_SEARCH = 13;
  private static final int METHODID_KEYWORD_SEARCH = 14;
  private static final int METHODID_EXPLAIN = 15;
  private static final int METHODID_DELE = 16;
  private static final int METHODID_RANGE_SEARCH = 17;
  private static final int METHODID_SORT = 18;
  private static final int METHODID_CREATE_DATABASE = 19;
  private static final int METHODID_DROP_DATABASE = 20;
  private static final int METHODID_LIST_DATABASES = 21;
  private static final int METHODID_DESCRIBE_DATABASE = 22;
  private static final int METHODID_GET_VERSION = 23;
  private static final int METHODID_ADD_INDEX = 24;
  private static final int METHODID_MODIFY_VECTOR_INDEX = 25;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SET_ALIAS:
          serviceImpl.setAlias((com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse>) responseObserver);
          break;
        case METHODID_GET_ALIAS:
          serviceImpl.getAlias((com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse>) responseObserver);
          break;
        case METHODID_DELETE_ALIAS:
          serviceImpl.deleteAlias((com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse>) responseObserver);
          break;
        case METHODID_CREATE_COLLECTION:
          serviceImpl.createCollection((com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse>) responseObserver);
          break;
        case METHODID_DROP_COLLECTION:
          serviceImpl.dropCollection((com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse>) responseObserver);
          break;
        case METHODID_TRUNCATE_COLLECTION:
          serviceImpl.truncateCollection((com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse>) responseObserver);
          break;
        case METHODID_DESCRIBE_COLLECTION:
          serviceImpl.describeCollection((com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse>) responseObserver);
          break;
        case METHODID_LIST_COLLECTIONS:
          serviceImpl.listCollections((com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse>) responseObserver);
          break;
        case METHODID_REBUILD_INDEX:
          serviceImpl.rebuildIndex((com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse>) responseObserver);
          break;
        case METHODID_UPSERT:
          serviceImpl.upsert((com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse>) responseObserver);
          break;
        case METHODID_UPDATE:
          serviceImpl.update((com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse>) responseObserver);
          break;
        case METHODID_QUERY:
          serviceImpl.query((com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse>) responseObserver);
          break;
        case METHODID_SEARCH:
          serviceImpl.search((com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse>) responseObserver);
          break;
        case METHODID_HYBRID_SEARCH:
          serviceImpl.hybridSearch((com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse>) responseObserver);
          break;
        case METHODID_KEYWORD_SEARCH:
          serviceImpl.keywordSearch((com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest>) responseObserver);
          break;
        case METHODID_EXPLAIN:
          serviceImpl.explain((com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse>) responseObserver);
          break;
        case METHODID_DELE:
          serviceImpl.dele((com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse>) responseObserver);
          break;
        case METHODID_RANGE_SEARCH:
          serviceImpl.rangeSearch((com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse>) responseObserver);
          break;
        case METHODID_SORT:
          serviceImpl.sort((com.tencent.tcvectordb.rpc.proto.Olama.SortRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.SortResponse>) responseObserver);
          break;
        case METHODID_CREATE_DATABASE:
          serviceImpl.createDatabase((com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse>) responseObserver);
          break;
        case METHODID_DROP_DATABASE:
          serviceImpl.dropDatabase((com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse>) responseObserver);
          break;
        case METHODID_LIST_DATABASES:
          serviceImpl.listDatabases((com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse>) responseObserver);
          break;
        case METHODID_DESCRIBE_DATABASE:
          serviceImpl.describeDatabase((com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse>) responseObserver);
          break;
        case METHODID_GET_VERSION:
          serviceImpl.getVersion((com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse>) responseObserver);
          break;
        case METHODID_ADD_INDEX:
          serviceImpl.addIndex((com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse>) responseObserver);
          break;
        case METHODID_MODIFY_VECTOR_INDEX:
          serviceImpl.modifyVectorIndex((com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest) request,
              (io.grpc.stub.StreamObserver<com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSetAliasMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.AddAliasRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse>(
                service, METHODID_SET_ALIAS)))
        .addMethod(
          getGetAliasMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.GetAliasRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.GetAliasResponse>(
                service, METHODID_GET_ALIAS)))
        .addMethod(
          getDeleteAliasMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.RemoveAliasRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.UpdateAliasResponse>(
                service, METHODID_DELETE_ALIAS)))
        .addMethod(
          getCreateCollectionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.CreateCollectionResponse>(
                service, METHODID_CREATE_COLLECTION)))
        .addMethod(
          getDropCollectionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.DropCollectionResponse>(
                service, METHODID_DROP_COLLECTION)))
        .addMethod(
          getTruncateCollectionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.TruncateCollectionResponse>(
                service, METHODID_TRUNCATE_COLLECTION)))
        .addMethod(
          getDescribeCollectionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.DescribeCollectionResponse>(
                service, METHODID_DESCRIBE_COLLECTION)))
        .addMethod(
          getListCollectionsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.ListCollectionsResponse>(
                service, METHODID_LIST_COLLECTIONS)))
        .addMethod(
          getRebuildIndexMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.RebuildIndexResponse>(
                service, METHODID_REBUILD_INDEX)))
        .addMethod(
          getUpsertMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.UpsertRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.UpsertResponse>(
                service, METHODID_UPSERT)))
        .addMethod(
          getUpdateMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.UpdateRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.UpdateResponse>(
                service, METHODID_UPDATE)))
        .addMethod(
          getQueryMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.QueryRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.QueryResponse>(
                service, METHODID_QUERY)))
        .addMethod(
          getSearchMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse>(
                service, METHODID_SEARCH)))
        .addMethod(
          getHybridSearchMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse>(
                service, METHODID_HYBRID_SEARCH)))
        .addMethod(
          getKeywordSearchMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest>(
                service, METHODID_KEYWORD_SEARCH)))
        .addMethod(
          getExplainMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.ExplainRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.ExplainResponse>(
                service, METHODID_EXPLAIN)))
        .addMethod(
          getDeleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.DeleteRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.DeleteResponse>(
                service, METHODID_DELE)))
        .addMethod(
          getRangeSearchMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.SearchRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.SearchResponse>(
                service, METHODID_RANGE_SEARCH)))
        .addMethod(
          getSortMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.SortRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.SortResponse>(
                service, METHODID_SORT)))
        .addMethod(
          getCreateDatabaseMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse>(
                service, METHODID_CREATE_DATABASE)))
        .addMethod(
          getDropDatabaseMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse>(
                service, METHODID_DROP_DATABASE)))
        .addMethod(
          getListDatabasesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.DatabaseRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.DatabaseResponse>(
                service, METHODID_LIST_DATABASES)))
        .addMethod(
          getDescribeDatabaseMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.DescribeDatabaseResponse>(
                service, METHODID_DESCRIBE_DATABASE)))
        .addMethod(
          getGetVersionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.GetVersionRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.GetVersionResponse>(
                service, METHODID_GET_VERSION)))
        .addMethod(
          getAddIndexMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.AddIndexRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.AddIndexResponse>(
                service, METHODID_ADD_INDEX)))
        .addMethod(
          getModifyVectorIndexMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexRequest,
              com.tencent.tcvectordb.rpc.proto.Olama.ModifyVectorIndexResponse>(
                service, METHODID_MODIFY_VECTOR_INDEX)))
        .build();
  }

  private static abstract class SearchEngineBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SearchEngineBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.tencent.tcvectordb.rpc.proto.Olama.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SearchEngine");
    }
  }

  private static final class SearchEngineFileDescriptorSupplier
      extends SearchEngineBaseDescriptorSupplier {
    SearchEngineFileDescriptorSupplier() {}
  }

  private static final class SearchEngineMethodDescriptorSupplier
      extends SearchEngineBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SearchEngineMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SearchEngineGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SearchEngineFileDescriptorSupplier())
              .addMethod(getSetAliasMethod())
              .addMethod(getGetAliasMethod())
              .addMethod(getDeleteAliasMethod())
              .addMethod(getCreateCollectionMethod())
              .addMethod(getDropCollectionMethod())
              .addMethod(getTruncateCollectionMethod())
              .addMethod(getDescribeCollectionMethod())
              .addMethod(getListCollectionsMethod())
              .addMethod(getRebuildIndexMethod())
              .addMethod(getUpsertMethod())
              .addMethod(getUpdateMethod())
              .addMethod(getQueryMethod())
              .addMethod(getSearchMethod())
              .addMethod(getHybridSearchMethod())
              .addMethod(getKeywordSearchMethod())
              .addMethod(getExplainMethod())
              .addMethod(getDeleMethod())
              .addMethod(getRangeSearchMethod())
              .addMethod(getSortMethod())
              .addMethod(getCreateDatabaseMethod())
              .addMethod(getDropDatabaseMethod())
              .addMethod(getListDatabasesMethod())
              .addMethod(getDescribeDatabaseMethod())
              .addMethod(getGetVersionMethod())
              .addMethod(getAddIndexMethod())
              .addMethod(getModifyVectorIndexMethod())
              .build();
        }
      }
    }
    return result;
  }
}
