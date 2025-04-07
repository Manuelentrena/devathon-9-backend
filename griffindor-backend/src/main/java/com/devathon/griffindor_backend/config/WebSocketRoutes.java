package com.devathon.griffindor_backend.config;

public class WebSocketRoutes {
    public static final String ENDPOINT_APP = "/connect-game";

    // Prefix
    public static final String GET_PREFIX = "/app";
    public static final String TOPIC_PREFIX = "/topic";
    public static final String QUEUE_PREFIX = "/queue";
    public static final String USER_PREFIX = "/user";

    // Actions
    public static final String TOKEN_ID = "/token-id";
    public static final String NUM_PLAYERS = "/num-players";
    public static final String LIST_PLAYERS = "/list-players";
    public static final String REGISTER_USER = "/register-user";
    public static final String DUEL = "/duel";
    public static final String CAST_SPELL = DUEL + "/cast-spells";

    // Error Path
    public static final String ERRORS = "/errors";

    // Get Paths
    public static final String GET_NUM_PLAYERS = GET_PREFIX + NUM_PLAYERS;
    public static final String GET_LIST_PLAYERS = GET_PREFIX + LIST_PLAYERS;
    public static final String POST_REGISTER_USER = GET_PREFIX + REGISTER_USER;

    // Topic Paths
    public static final String TOPIC_NUM_PLAYERS = TOPIC_PREFIX + NUM_PLAYERS;
    public static final String TOPIC_LIST_PLAYERS = TOPIC_PREFIX + LIST_PLAYERS;

    // Queue Paths
    public static final String QUEUE_TOKEN_ID = QUEUE_PREFIX + TOKEN_ID;
    public static final String QUEUE_REGISTER_USER = QUEUE_PREFIX + REGISTER_USER;
    public static final String QUEUE_ERRORS = QUEUE_PREFIX + ERRORS;
    public static final String QUEUE_LIST_PLAYERS = QUEUE_PREFIX + LIST_PLAYERS;
    public static final String QUEUE_DUEL = QUEUE_PREFIX + DUEL;
    public static final String QUEUE_DUEL_RESULT = QUEUE_DUEL + "/result";

    // Queue Paths Subscriber
    public static final String USER_QUEUE_LIST_PLAYERS = USER_PREFIX + QUEUE_PREFIX + LIST_PLAYERS;

}
