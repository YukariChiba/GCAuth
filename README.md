# GCAuth-OpenID

Grasscutter Authentication System (Based on OpenID)

### Version Compatibility

| GCAuth-OpenID | Grasscutter Development | Grasscutter Stable |
|---------------|-------------------------|--------------------|
| 0.1.0 - 0.5.0 | 1.1.2-dev               | -                  |

### Usage : 

#### Step 1: Install

Place plugin jar file to your `plugin` folder.

#### Step 2: Modify config

Start the server and stop it. You'll find `plugins/GCAuth-OpenID/config.json`. Modify the config:

- `client_id` and `client_secret`: Your OIDC Client ID and secret.
- `auth_endpoint` and `token_endpoint`: OIDC API to request authentication.
- `redirect_uri`: `https://YOUR_SERVER/authentication/openid/redirect`.
- `username_claim`: Claim field to use as an username.
- `group_claim`: Claim field to use as groups.
- `default_permission_nodes`: Default permission nodes.
- `permission_settings`: Permission settings per group.
    - `group_id`: Group name.
    - `permission_nodes`: Permission nodes for this group.

#### Step 3: Run

Start the server again and enjoy!