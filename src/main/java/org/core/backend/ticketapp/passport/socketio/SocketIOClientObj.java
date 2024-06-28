package org.core.backend.ticketapp.passport.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketIOClientObj implements Serializable {
    private SocketIOClient socketIOClient;
}
