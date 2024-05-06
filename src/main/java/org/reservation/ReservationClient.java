package org.reservation;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ReservationClient {
	private final ManagedChannel channel;
	private final org.reservation.ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub;

	public ReservationClient(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port)
				.usePlaintext()
				.build();
		this.blockingStub = org.reservation.ReservationServiceGrpc.newBlockingStub(channel);
	}

	public void addItem(String itemName, double price, int quantity) {
		org.reservation.ItemRequest request = org.reservation.ItemRequest.newBuilder()
				.setItemName(itemName)
				.setPrice(price)
				.setQuantity(quantity)
				.build();
		org.reservation.ItemResponse response = blockingStub.addItem(request);
		System.out.println("Item added, ID: " + response.getItemId());
	}
	public void updateItem(String itemId, String itemName, double price, int quantity) {
		org.reservation.ItemUpdateRequest request = org.reservation.ItemUpdateRequest.newBuilder()
				.setItemId(itemId)
				.build();
		org.reservation.ItemResponse response = blockingStub.updateItem(request);
		System.out.println("Item updated, ID: " + response.getItemId());
	}
	public void deleteItem(String itemId) {
		org.reservation.ItemDeleteRequest request = org.reservation.ItemDeleteRequest.newBuilder()
				.setItemId(itemId)
				.build();
		org.reservation.ItemResponse response = blockingStub.deleteItem(request);
		System.out.println("Item deleted, ID: " + response.getItemId());
	}
	public void updateInventory(String itemId, int quantity) {
		org.reservation.InventoryUpdateRequest request = org.reservation.InventoryUpdateRequest.newBuilder()
				.setItemId(itemId)
				.setQuantity(quantity)
				.build();
		org.reservation.InventoryUpdateResponse response = blockingStub.updateInventory(request);
		System.out.println(response.getMessage());
	}

	// TODO: Implement other client methods (updateItem, deleteItem, updateInventory, browseItems, makeReservation, placeOrder)

	public static void main(String[] args) {
		ReservationClient client = new ReservationClient("localhost", 50050);
		client.addItem("Sample Item", 10.0, 5);
		// Call other client methods as needed
	}
}
