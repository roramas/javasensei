package javasensei.db.managments;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javasensei.db.Connection;
import javasensei.util.FileHelper;

/**
 *
 * @author oramas
 */
public class ExamenesManager {

    private static final DBCollection pretestCollection = Connection.getCollection().get(CollectionsDB.PRETEST);
    private static final DBCollection posttestCollection = Connection.getCollection().get(CollectionsDB.POSTTEST);
    
    //Respuestas de los cuestionarios
    static final DBObject jsonRespuestasPreTestA = (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_pretest.json"));
    
    static final DBObject jsonRespuestasPreTestB = (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_pretestB.json"));
    
    static final DBObject jsonRespuestasPreTestC = (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_pretestC.json"));
    
    static final DBObject jsonRespuestasPostTestA = (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_posttest.json"));
    
    static final DBObject jsonRespuestasPostTestB = (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_posttestB.json"));
    
    static final DBObject jsonRespuestasPostTestC = (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_posttestC.json"));
    
    
    public String calificarExamenPreTest(String idFacebook, String jsonRespuestas, String tipoExamen) {

        DBObject jsonRespuestasPreTest = new BasicDBObject();
        
        switch (tipoExamen) {
            case "A":
                jsonRespuestasPreTest = jsonRespuestasPreTestA;
                break;
            case "B":
                jsonRespuestasPreTest = jsonRespuestasPreTestB;
                break;
            case "C":
                jsonRespuestasPreTest = jsonRespuestasPreTestC;
                break;
        }

        double cantidadPreguntas = jsonRespuestasPreTest.keySet().size();

        BasicDBList examen = (BasicDBList) com.mongodb.util.JSON.parse(jsonRespuestas);

        DBObject insert = new BasicDBObject();

        if (examen.size() > 0) {

            double totalRespuestasCorrectas = 0;

            for (Object jsonRespuesta : examen.toArray()) {
                DBObject json = (DBObject) jsonRespuesta;

                String pregunta = json.get("pregunta").toString();
                String respuesta = json.get("respuesta").toString();

                if (jsonRespuestasPreTest.get(pregunta).equals(respuesta)) {
                    totalRespuestasCorrectas++;
                }
            }

            double promedio = totalRespuestasCorrectas / cantidadPreguntas;

            insert.put("idFacebook", idFacebook);
            insert.put("examen", examen);
            insert.put("tipoExamen", tipoExamen);
            insert.put("totalRespuestasCorrectas", totalRespuestasCorrectas);
            insert.put("promedio", promedio);
            insert.put("cantidadPreguntas", cantidadPreguntas);
            LocalDateTime dateTime = LocalDateTime.now();
            insert.put("fecha", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
            insert.put("hora", dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));

            pretestCollection.save(insert);
        }

        return insert.toString();
    }

    public String realizoExamenPreTest(String idFacebook) {
        DBObject result = pretestCollection.findOne(
                new BasicDBObject("idFacebook", idFacebook)
        );
        return new BasicDBObject("realizado", result != null).toJson();
    }
    
    public String calificarExamenPostTest(String idFacebook, String jsonRespuestas, String tipoExamen) {

        DBObject jsonRespuestasPostTest = new BasicDBObject();
        
        switch (tipoExamen) {
            case "A":
                jsonRespuestasPostTest = jsonRespuestasPostTestA;
                break;
            case "B":
                jsonRespuestasPostTest = jsonRespuestasPostTestB;
                break;
            case "C":
                jsonRespuestasPostTest = jsonRespuestasPostTestC;
                break;
        }

        double cantidadPreguntas = jsonRespuestasPostTest.keySet().size();

        BasicDBList examen = (BasicDBList) com.mongodb.util.JSON.parse(jsonRespuestas);

        DBObject insert = new BasicDBObject();

        if (examen.size() > 0) {

            double totalRespuestasCorrectas = 0;

            for (Object jsonRespuesta : examen.toArray()) {
                DBObject json = (DBObject) jsonRespuesta;

                String pregunta = json.get("pregunta").toString();
                String respuesta = json.get("respuesta").toString();

                if (jsonRespuestasPostTest.get(pregunta).equals(respuesta)) {
                    totalRespuestasCorrectas++;
                }
            }

            double promedio = totalRespuestasCorrectas / cantidadPreguntas;

            insert.put("idFacebook", idFacebook);
            insert.put("examen", examen);
            insert.put("tipoExamen", tipoExamen);
            insert.put("totalRespuestasCorrectas", totalRespuestasCorrectas);
            insert.put("promedio", promedio);
            insert.put("cantidadPreguntas", cantidadPreguntas);
            LocalDateTime dateTime = LocalDateTime.now();
            insert.put("fecha", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
            insert.put("hora", dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));

            posttestCollection.save(insert);
        }

        return insert.toString();
    }
    
    public String realizoExamenPostTest(String idFacebook) {
        DBObject result = posttestCollection.findOne(
                new BasicDBObject("idFacebook", idFacebook)
        );
        return new BasicDBObject("realizado", result != null).toJson();
    }
}
