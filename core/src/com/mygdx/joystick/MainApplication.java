package com.mygdx.joystick;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainApplication extends ApplicationAdapter implements MqttCallback {

	private static final String brokerUrl = "tcp://192.168.1.2:1883";
	private static final String clientId = "random-client-id";
	private static final String topic = "test";
	private MqttClient sampleClient = null;
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private int SCALE = 100;
	private Cross cross = new Cross(26, 7, 75, -5);

	@Override
	public void create() {
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(SCALE, SCALE * (h / w));
		shapeRenderer = new ShapeRenderer();
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		// subscribe();
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				int buttonPressed = cross.getButtonPressed(touchPos.x, touchPos.y);

				switch (buttonPressed) {
					case Cross.Button.DOWN:
						sendCmd("bajar");
						break;
					case Cross.Button.UP:
						sendCmd("subir");
						break;
					case Cross.Button.LEFT:
						sendCmd("yaw-izq");
						break;

					case Cross.Button.RIGHT:
						sendCmd("yaw-der");
						break;
					default:
						break;
				}
				return true;
			}
		});
	}

	@Override
	public void render() {
		camera.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapeRenderer.setProjectionMatrix(camera.combined);
		cross.render(shapeRenderer);

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = SCALE;
		camera.viewportHeight = SCALE * height / width;
		camera.update();
	}

	private void sendCmd(String cmd) {
		final MqttMessage message = new MqttMessage();
		message.setPayload(cmd.getBytes());
		/*
		 * try {
		 * sampleClient.publish("topic", message);
		 * } catch (MqttException e) {
		 * e.printStackTrace();
		 * }
		 */
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

	@Override
	public void connectionLost(Throwable cause) {

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {

	}

	private void subscribe() {
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			sampleClient = new MqttClient(brokerUrl, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("checking");
			System.out.println("Mqtt Connecting to broker: " + brokerUrl);
			sampleClient.connect(connOpts);
			System.out.println("Mqtt Connected");
			sampleClient.setCallback(this);
			sampleClient.subscribe(topic);
			System.out.println("Subscribed");
			System.out.println("Listening");
		} catch (MqttException me) {
			System.out.println(me.getCause().toString());
		}
	}
}
