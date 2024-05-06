package org.reservation;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.reservation.service.ItemServiceImpl;
import org.reservation.service.ReservationServiceImpl;
import org.reservation.service.UserServiceImpl;

public class ReservationServer {

	public static void main(String[] args) {
		int serverPort = 9099;

		Server server = ServerBuilder
				.forPort(serverPort)
				.addService(new ItemServiceImpl())
				.addService(new ReservationServiceImpl())
				.addService(new UserServiceImpl())
				.build();

		try {
			server.start();
			System.out.println("Server started, listening on " + serverPort);
			server.awaitTermination();
		} catch (Exception e) {
			System.out.println("Server error: " + e.getMessage());
		}
	}
}
