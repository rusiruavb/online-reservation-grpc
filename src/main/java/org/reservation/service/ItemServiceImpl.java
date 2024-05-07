package org.reservation.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.item.*;
import org.reservation.ReservationServer;

import java.util.Map;

public class ItemServiceImpl extends ItemServiceGrpc.ItemServiceImplBase {
    private Map<Integer, AddItemRequest> items;

    private ReservationServer server;

    public ItemServiceImpl(ReservationServer server) {
        this.server = server;
    }

    @Override
    public void addItem(AddItemRequest request, StreamObserver<AddItemResponse> responseObserver) {
        items.put(request.getItemId(), request);

        AddItemResponse response = AddItemResponse.newBuilder()
                .setItemId(request.getItemId())
                .setQuantity(request.getQuantity())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateItem(UpdateItemRequest request, StreamObserver<UpdateItemResponse> responseObserver) {
        if (server.isLeader()) {
            UpdateItemResponse response = UpdateItemResponse.newBuilder()
                    .setItemId(request.getItemId())
                    .setQuantity(request.getQuantity())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Not a leader"));
        }
    }

    @Override
    public void updateQuantity(UpdateQuantityRequest request, StreamObserver<UpdateItemResponse> responseObserver) {
        if (server.isLeader()) {
            UpdateItemResponse response = UpdateItemResponse.newBuilder()
                    .setItemId(request.getItemId())
                    .setQuantity(request.getQuantity())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Not a leader"));
        }
    }

    @Override
    public void listItems(Empty request, StreamObserver<ListItemsResponse> responseObserver) {
        ListItemsResponse.Builder response = ListItemsResponse.newBuilder();

        for (AddItemRequest item : items.values()) {
            response.addItems(ListItemResponse.newBuilder()
                    .setItemId(item.getItemId())
                    .setQuantity(item.getQuantity())
                    .build());
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeItem(RemoveItemRequest request, StreamObserver<RemoveItemResponse> responseObserver) {
        if (server.isLeader()) {
            RemoveItemResponse response = RemoveItemResponse.newBuilder()
                    .setMessage(request.getItemId() + " removed")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Not a leader"));
        
        }
    }
}
