package com.devathon.griffindor_backend.config;

public class WebSocketRoutes {
    public static final String ENDPOINT_APP = "/connect-game";

    // Prefix
    public static final String GET_PREFIX = "/app";
    public static final String TOPIC_PREFIX = "/topic";
    public static final String QUEUE_PREFIX = "/queue";
    public static final String USER_PREFIX = "/user";

    // Actions
    public static final String SESSION_ID = "/session-id";
    public static final String NUM_PLAYERS = "/num-players";
    public static final String LIST_PLAYERS = "/list-players";

    // Get Paths
    public static final String GET_NUM_PLAYERS = GET_PREFIX + NUM_PLAYERS;
    public static final String GET_LIST_PLAYERS = GET_PREFIX + LIST_PLAYERS;

    // Topic Paths
    public static final String TOPIC_NUM_PLAYERS = TOPIC_PREFIX + NUM_PLAYERS;
    public static final String TOPIC_LIST_PLAYERS = TOPIC_PREFIX + LIST_PLAYERS;

    // Queue Paths
    public static final String QUEUE_SESSION_ID = QUEUE_PREFIX + SESSION_ID;
}
