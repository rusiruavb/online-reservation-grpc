package org.reservation.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.item.*;
import org.reservation.ReservationServer;

import java.util.HashMap;
import java.util.Map;

public class ItemServiceImpl extends ItemServiceGrpc.ItemServiceImplBase {
    private Map<Integer, AddItemRequest> items = new HashMap<>();

    private ReservationServer server;

    public ItemServiceImpl(ReservationServer server) {
        this.server = server;
    }

    @Override
    public void addItem(AddItemRequest request, StreamObserver<AddItemResponse> responseObserver) {
        System.out.println("Add item request received : " + request.getItemId());
        System.out.println(request.getPrice());
        AddItemRequest item = AddItemRequest.newBuilder()
                .setItemId(request.getItemId())
                .setPrice(request.getPrice())
                .setQuantity(request.getQuantity())
                .setName(request.getName())
                .setDescription(request.getDescription())
                .build();

        AddItemResponse response = AddItemResponse.newBuilder()
                .setItemId(request.getItemId())
                .setName(request.getName())
                .setQuantity(request.getQuantity())
                .build();

        System.out.println("Item added : " + item.getItemId());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateItem(UpdateItemRequest request, StreamObserver<UpdateItemResponse> responseObserver) {
        System.out.println("Update item request received : " + request.getItemId());
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
        System.out.println("Update quantity request received : " + request.getItemId());
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
        System.out.println("List items request received");
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
        System.out.println("Remove item request received : " + request.getItemId());
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
