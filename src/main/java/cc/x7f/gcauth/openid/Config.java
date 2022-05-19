package cc.x7f.gcauth.openid;

public final class Config {
    public String client_id = "ReplaceMe";
    public String client_secret = "ReplaceMe";
    public String auth_endpoint = "https://YOUR_DOMAIN/auth/realms/YOUR_REALM/protocol/openid-connect/auth";
    public String token_endpoint = "https://YOUR_DOMAIN/auth/realms/YOUR_REALM/protocol/openid-connect/token";
    public String redirect_uri = "https://YOUR_SERVER/authentication/openid/redirect";
    public String username_claim = "username";
    public String group_claim = "";
    public String[] default_permission_nodes = {};
    public PermissionSettings[] permission_settings = {};

    public static class PermissionSettings {
        public String group_id;
        public String[] permission_nodes;
    }
}
