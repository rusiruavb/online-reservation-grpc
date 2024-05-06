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
        if (server.isLeader()) {
            items.put(request.getItemId(), request);
            // TODO: Add item to the database
        }

    }

    @Override
    public void updateItem(UpdateItemRequest request, StreamObserver<UpdateItemResponse> responseObserver) {
        super.updateItem(request, responseObserver);
    }

    @Override
    public void updateQuantity(UpdateQuantityRequest request, StreamObserver<UpdateItemResponse> responseObserver) {
        super.updateQuantity(request, responseObserver);
    }

    @Override
    public void listItems(Empty request, StreamObserver<ListItemsResponse> responseObserver) {
        super.listItems(request, responseObserver);
    }

    @Override
    public void removeItem(RemoveItemRequest request, StreamObserver<RemoveItemResponse> responseObserver) {
        super.removeItem(request, responseObserver);
    }
}
