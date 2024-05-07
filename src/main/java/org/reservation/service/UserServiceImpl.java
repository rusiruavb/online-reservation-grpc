package org.reservation.service;

import io.grpc.stub.StreamObserver;

import java.util.Map;

import org.user.*;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private Map<String, CreateUserRequest> users;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {
        System.out.println("Create user request received" + request.getUserName());
        CreateUserRequest user = CreateUserRequest.newBuilder()
            .setUserName(request.getUserName())
            .setRole(request.getRole())
            .setPassword(request.getPassword())
            .build();

        users.put(request.getUserName(), user);

        CreateUserResponse response = CreateUserResponse.newBuilder()
                .setUserName(user.getUserName())
                .setRole(user.getRole())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void loginUser(LoginUserRequest request, StreamObserver<LoginUserResponse> responseObserver) {
        System.out.println("Login request received" + request.getUserName());
        if (users.containsKey(request.getUserName())) {
            CreateUserRequest user = users.get(request.getUserName());
            if (user.getPassword().equals(request.getPassword())) {
                LoginUserResponse response = LoginUserResponse.newBuilder()
                        .setUserName(user.getUserName())
                        .setRole(user.getRole())
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(new RuntimeException("Invalid password"));
            }
        } else {
            responseObserver.onError(new RuntimeException("User not found"));
        }
    }
}
