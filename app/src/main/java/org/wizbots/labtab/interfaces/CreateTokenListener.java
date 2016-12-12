package org.wizbots.labtab.interfaces;

import org.wizbots.labtab.model.CreateTokenResponse;

public interface CreateTokenListener extends BaseUIListener {
    void tokenCreatedSuccessfully(CreateTokenResponse createTokenResponse);

    void unableToCreateToken(int responseCode);
}
