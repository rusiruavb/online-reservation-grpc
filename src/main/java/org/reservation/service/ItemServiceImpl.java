package org.reservation.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.item.*;

public class ItemServiceImpl extends ItemServiceGrpc.ItemServiceImplBase {
    @Override
    public void addItem(AddItemRequest request, StreamObserver<AddItemResponse> responseObserver) {
        super.addItem(request, responseObserver);
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
