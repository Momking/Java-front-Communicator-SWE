package com.swe.controller;

//import com.conferencing.App;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.swe.controller.RPCinterface.AbstractRPC;

public class controller {
    public static void main(String[] args) throws InterruptedException {

        AbstractRPC rpc = new RPC();

        controllerServices services = new controllerServices(rpc);

        Thread handler;
        try {
            handler = rpc.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final Thread controllerThread = new Thread(services::runController);
        controllerThread.start();

        handler.join();
    }
}