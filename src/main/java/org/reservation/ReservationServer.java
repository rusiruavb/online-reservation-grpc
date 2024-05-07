package org.reservation;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.zookeeper.KeeperException;
import org.nameserver.NameServiceClient;
import org.reservation.service.ItemServiceImpl;
import org.reservation.service.ReservationServiceImpl;
import org.reservation.service.UserServiceImpl;
import org.synchronization.lock.DistributedLock;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReservationServer {
	private final int serverPort;
	private final DistributedLock leaderLock;
	private final AtomicBoolean isLeader = new AtomicBoolean(false);
	private byte[] leaderData;
	public static final String NAME_SERVICE_ADDRESS = "http://localhost:2379";

	public ReservationServer(String host, int port) throws InterruptedException, IOException, KeeperException {
		this.serverPort = port;
		leaderLock = new DistributedLock("reservation", buildServerData(host, port));
	}

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		DistributedLock.setZooKeeperURL("localhost:2181");

		if (args.length != 2) {
			System.out.println("Usage: ReservationServer <host> <port>");
			System.exit(1);
		}

		String host = args[0] != null ? args[0] : "localhost";
		Integer port = args[1] != null ? Integer.parseInt(args[1]) : 50051;

		ReservationServer server = new ReservationServer(host, port);
		server.startServer();
	}

	public static String buildServerData(String IP, int port) {
		StringBuilder builder = new StringBuilder();
		builder.append(IP).append(":").append(port);
		return builder.toString();
	}

	public boolean isLeader() {
		return isLeader.get();
	}
	private synchronized void setCurrentLeaderData(byte[] leaderData) {
		this.leaderData = leaderData;
	}

	class LeaderCampaignThread implements Runnable {
		private byte[] currentLeaderData = null;
		@Override
		public void run() {
			System.out.println("Starting the leader Campaign");
			try {
				boolean leader = leaderLock.tryAcquireLock();
				while (!leader) {
					byte[] leaderData =
							leaderLock.getLockHolderData();
					if (currentLeaderData != leaderData) {
						currentLeaderData = leaderData;
						setCurrentLeaderData(currentLeaderData);
					}
					Thread.sleep(10000);
					leader = leaderLock.tryAcquireLock();
				}
				System.out.println("I got the leader lock. Now acting as primary");
				isLeader.set(true);
				currentLeaderData = null;
			} catch (Exception e){
				System.out.println("Error while trying to acquire the lock: " + e.getMessage());
			}
		}
	}

	private void tryToBeLeader() throws KeeperException, InterruptedException {
		Thread leaderCampaignThread = new Thread(new LeaderCampaignThread());
		leaderCampaignThread.start();
	}

	public void startServer() {
		Server server = ServerBuilder
				.forPort(this.serverPort)
				.addService(new ItemServiceImpl(this))
				.addService(new ReservationServiceImpl())
				.addService(new UserServiceImpl())
				.build();

		try {
			server.start();
			NameServiceClient nameServiceClient = new NameServiceClient(NAME_SERVICE_ADDRESS);
			nameServiceClient.registerService("reservation", "localhost", serverPort, "grpc");
			System.out.println("Server started, listening on " + serverPort);
			tryToBeLeader(); // Start the Thread to campaign for leader
			server.awaitTermination();
		} catch (Exception e) {
			System.out.println("Server error: " + e.getMessage());
		}
	}
}
