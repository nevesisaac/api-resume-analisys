package org.acme.service.rabbitmq;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.quarkus.logging.Log;

@ApplicationScoped
public class ProducerRabbitMQ {

    @Inject
    @Channel("meu-topico")
    Emitter<String> emitter;

    public void enviarMensagem(String conteudo) {
        emitter.send(conteudo);
        Log.info("Mensagem publicada: " + conteudo);
    }
}