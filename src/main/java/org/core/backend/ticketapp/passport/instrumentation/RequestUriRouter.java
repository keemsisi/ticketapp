package org.core.backend.ticketapp.passport.instrumentation;

import java.util.ArrayList;
import java.util.List;

public class RequestUriRouter {

    private static final String URI_PATH_SEPARATOR = "/";
    private static final String MULTI_WILDCARD = "*";
    private static final String SINGLE_WILDCARD = "?";
    private static final String DOT = ".";

    private final List<Route> exactRoutes = new ArrayList<>();
    private final List<Route> patternRoutes = new ArrayList<>();
    private final List<Route> extensionRoutes = new ArrayList<>();

    public RequestUriRouter addRoute(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        Route route = createRoute(path);
        switch (route.routeMatchType) {
            case EXACT:
                exactRoutes.add(route);
                break;
            case PATTERN:
                patternRoutes.add(route);
                break;
            case EXTENSION:
                extensionRoutes.add(route);
                break;
        }
        return this;
    }

    private Route createRoute(String route) {
        if (route.startsWith(URI_PATH_SEPARATOR)) {
            route = route.substring(1);
        }
        if (route.contains(MULTI_WILDCARD)) {
            route = cleanupExcessWildCards(route);
        }
        if (route.contains(MULTI_WILDCARD) || route.contains(SINGLE_WILDCARD)) {
            if (route.length() > 2 && route.startsWith(MULTI_WILDCARD + DOT)) {
                return new Route(route.substring(2), RouteMatchType.EXTENSION);
            }
            return new Route(route, RouteMatchType.PATTERN);
        } else {
            return new Route(route, RouteMatchType.EXACT);
        }
    }

    private String cleanupExcessWildCards(String route) {

        route = route.replaceAll("(\\*/){2,}", "*/");
        route = route.replaceAll("(\\*){2,}", "*");
        return route;
    }

    public boolean pathMatchesRoute(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        if (path.startsWith(URI_PATH_SEPARATOR)) {
            path = path.substring(1);
        }
        Route route = matchExactRoute(path);
        if (route != null) {
            return true;
        }
        route = matchPatternRoute(path);
        if (route != null) {
            return true;
        }
        route = matchExtensionRoute(path);
        return route != null;
    }

    public Route getRouteMatch(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        if (path.startsWith(URI_PATH_SEPARATOR)) {
            path = path.substring(1);
        }
        Route route = matchExactRoute(path);
        if (route != null) {
            return route;
        }
        route = matchPatternRoute(path);
        if (route != null) {
            return route;
        }
        route = matchExtensionRoute(path);
        return route;
    }

    private Route matchExactRoute(String path) {
        for (Route exactRoute : exactRoutes) {
            if (exactRoute.route.equals(path)) {
                return exactRoute;
            }
        }
        return null;
    }

    private Route matchPatternRoute(String path) {
        Route matchFound = null;
        for (Route patternRoute : patternRoutes) {
            String[] routeTokens = patternRoute.route.split(URI_PATH_SEPARATOR);
            String[] pathTokens = path.split(URI_PATH_SEPARATOR);
            if (routeTokens != null && routeTokens.length > 0 && pathTokens != null && pathTokens.length > 0) {
                int i = 0, j = 0;
                boolean misMatchFound = false;
                for (; i < routeTokens.length && j < pathTokens.length; i++, j++) {
                    if (MULTI_WILDCARD.equals(routeTokens[i])) {
                        i++;
                        if (i < routeTokens.length) {
                            while (j < pathTokens.length && !SINGLE_WILDCARD.equals(routeTokens[i]) && !routeTokens[i].equals(pathTokens[j])) { //skip other characters to  next match
                                j++;
                            }
                            if (j >= pathTokens.length) {
                                misMatchFound = true;
                                break;
                            }
                        } else {//last character is a *
                            j = pathTokens.length;
                            break;
                        }
                    }

                    if (!SINGLE_WILDCARD.equals(routeTokens[i]) && !routeTokens[i].equals(pathTokens[j])) {
                        misMatchFound = true;
                        break;
                    }
                }

                if (!misMatchFound && j >= pathTokens.length && i >= routeTokens.length) {
                    matchFound = patternRoute;
                }
            }

            if (matchFound != null) {
                break;
            }
        }

        return matchFound;
    }

    private Route matchExtensionRoute(String path) {
        int extensionPosition = path.indexOf(DOT) + 1;
        if (extensionPosition > 0) {
            String extension = path.substring(extensionPosition);
            for (Route extensionRoute : extensionRoutes) {
                if (extensionRoute.route.equals(extension) || extensionRoute.route.equals(MULTI_WILDCARD)) {
                    return extensionRoute;
                }
            }
        }
        return null;
    }

    private enum RouteMatchType {
        EXACT, PATTERN, EXTENSION
    }

    private class Route {

        private final String route;
        private final RouteMatchType routeMatchType;

        public Route(String route, RouteMatchType routeMatchType) {
            this.route = route;
            this.routeMatchType = routeMatchType;
        }

    }
}
