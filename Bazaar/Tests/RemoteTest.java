package Tests;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import Client.Referee;
import Player.MaximizeCards;
import Player.Mechanism;
import Server.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoteTest {
  @Test
  void testMethodCalls() {
    Gson gson = new Gson();
    List<Object> winMethod = new ArrayList<>();
    winMethod.add("win");
    List<Boolean> args = new ArrayList<>();
    args.add(true);
    winMethod.add(args);

    String methodJson = gson.toJson(winMethod);
    System.out.println(methodJson);
    InputStream inputStream = new ByteArrayInputStream(methodJson.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    Referee proxyRef = new Referee(new Mechanism("bob", new MaximizeCards()), inputStream, outputStream);
    proxyRef.completeInteraction();

    assertEquals("void", outputStream.toString());
  }
}
