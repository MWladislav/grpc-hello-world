package org.example.grpchelloworld.service;

import io.grpc.stub.StreamObserver;
import org.example.grpchelloworld.GreetingPayload;
import org.example.grpchelloworld.GreetingResponse;
import org.example.grpchelloworld.GreetingServiceGrpc;

import java.util.logging.Logger;

public class SimpleGreetingService extends GreetingServiceGrpc.GreetingServiceImplBase {
    private static final Logger logger = Logger.getLogger(SimpleGreetingService.class.getName());

    @Override
    public void greeting(final GreetingPayload request,
                         final StreamObserver<GreetingResponse> responseObserver) {
        GreetingResponse response = GreetingResponse.newBuilder()
                .setMessage("Hello " + request.getUsername())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GreetingResponse> greetingClientStream(final StreamObserver<GreetingPayload> responseObserver) {
        return new StreamObserver<>() {
            int requestCount;

            @Override
            public void onNext(final GreetingResponse greetingResponse) {
                requestCount++;
                logger.info(
                        "Getting greeting message: %s, total messages: %d"
                                .formatted(greetingResponse.getMessage(), requestCount)
                );
            }

            @Override
            public void onError(final Throwable throwable) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void greetingServerStream(final GreetingPayload request, final StreamObserver<GreetingResponse> responseObserver) {
        for (int i = 0; i < 5; i++) {
            responseObserver.onNext(GreetingResponse
                    .newBuilder()
                    .setMessage("Hello %s for %d time".formatted(request.getUsername(), i))
                    .build());
        }
    }

    @Override
    public StreamObserver<GreetingResponse> greetingBiDirectionalStream(final StreamObserver<GreetingResponse> responseObserver) {
        return new StreamObserver<>() {
            int requestCount = 0;
            @Override
            public void onNext(final GreetingResponse greetingResponse) {
                responseObserver.onNext(GreetingResponse
                        .newBuilder()
                        .setMessage("%s_%d".formatted(greetingResponse.getMessage(), requestCount++))
                        .build()
                );
            }

            @Override
            public void onError(final Throwable throwable) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
