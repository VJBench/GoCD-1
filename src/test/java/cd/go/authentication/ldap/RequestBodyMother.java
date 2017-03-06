package cd.go.authentication.ldap;

import cd.go.authentication.ldap.executor.SearchUserExecutor;
import cd.go.authentication.ldap.model.AuthConfig;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestBodyMother {
    private static Gson gson = new Gson();

    public static String forSearch(String searchTerm, String searchBase) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(SearchUserExecutor.SEARCH_TERM, searchTerm);
        requestMap.put("auth_configs", Collections.singletonList(AuthConfig.fromJSON(authConfigJson("ldap", searchBase, ""))));
        return gson.toJson(requestMap);
    }

    public static String forSearch(String searchTerm) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(SearchUserExecutor.SEARCH_TERM, searchTerm);
        requestMap.put("auth_configs", Collections.singletonList(AuthConfig.fromJSON(authConfigJson("ldap", "ou=pune,ou=system", ""))));
        return gson.toJson(requestMap);
    }

    public static String forSearchWithSearchFilter(String searchTerm, String searchFilter) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(SearchUserExecutor.SEARCH_TERM, searchTerm);
        requestMap.put("auth_configs", Collections.singletonList(AuthConfig.fromJSON(authConfigJson("ldap", "ou=pune,ou=system", searchFilter))));
        return gson.toJson(requestMap);
    }

    public static String forSearchWithMultipleAuthConfigs(String searchTerm) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(SearchUserExecutor.SEARCH_TERM, searchTerm);
        requestMap.put("auth_configs", Arrays.asList(
                AuthConfig.fromJSON(authConfigJson("ldap-1", "ou=pune,ou=system", "")),
                AuthConfig.fromJSON(authConfigJson("ldap-2", "ou=pune,ou=system", ""))
        ));
        return gson.toJson(requestMap);
    }

    public static String forAuthenticate(String username, String password, String searchBase) {
        Map<String, Object> requestMap = getRequestBodyMap(username, password, "ldap", searchBase);
        return gson.toJson(requestMap);
    }

    private static Map<String, Object> getRequestBodyMap(String username, String password, String authConfigId, String searchBase) {
        Map<String, Object> requestMap = new HashMap<>();

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);
        requestMap.put("credentials", credentials);
        requestMap.put("auth_configs", Collections.singletonList(AuthConfig.fromJSON(authConfigJson(authConfigId, searchBase, ""))));
        requestMap.put("role_configs", Collections.emptyList());
        return requestMap;
    }


    private static String authConfigJson(String authConfigId, String searchBase, String searchFilter) {
        return String.format("{\n" +
                "  \"id\": \"%s\",\n" +
                "  \"configuration\": {\n" +
                "    \"ManagerDN\": \"uid=admin,ou=system\",\n" +
                "    \"DisplayNameAttribute\": \"displayName\",\n" +
                "    \"SearchBases\": \"%s\",\n" +
                "    \"SearchAttributes\": \"%s\",\n" +
                "    \"LoginAttribute\": \"uid\",\n" +
                "    \"Url\": \"ldap://localhost:10389\",\n" +
                "    \"Password\": \"secret\"\n" +
                "  }\n" +
                "}", authConfigId, searchBase, searchFilter);
    }
}
