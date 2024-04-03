package br.edu.ifrs.pitanga.core.infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LocalCompilerRunner {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public String execute()
    {
        compileJava("HelloWorld.java");
        return runJava("HelloWorld");
    }

    private String readErrorStream(Process process) {
        StringBuffer str = new StringBuffer();
        try (InputStreamReader isr = new InputStreamReader(process.getErrorStream());
            BufferedReader reader = new BufferedReader(isr)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line + '\n');
            }
        } catch (IOException e) {
            logger.error("Error on reading error stream :: ", e);
        }
        return str.toString();
    }

    private String readOutStream(Process process) {
        StringBuffer str = new StringBuffer();
        try (
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr)
        ){
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line + '\n');
            }
        } catch(IOException e) { logger.error("Error on reading output stream :: ", e); }
        return str.toString();
    }

    private void compileJava(String fileName) {
        logger.info("Compiling " + fileName);
        try {
            Process process = Runtime.getRuntime().exec("javac " + fileName);
            int exitCode = process.waitFor();
            if(exitCode > 0) {
                String error = readErrorStream(process);
                logger.error(error);
            }
            logger.info(fileName + " has compiled");
        } catch (IOException | InterruptedException e) {
            logger.error("Error on compiling solution :: ", e);
        }
    }

    private String runJava(String className) {
        logger.info("Running " + className);
        try {
            Process process = Runtime.getRuntime().exec("java " + className);
            int exitCode = process.waitFor();
            if(exitCode > 0) {
                logger.info("Error on executing :: " + className);
                String error = readErrorStream(process);
                logger.error(error);
                return error;
            }
            logger.info("Executed with success -> " + className);
            return readOutStream(process);
        } catch (IOException | InterruptedException e) {
            logger.error("Error on running solution :: ", e);
            return "ERROR";
        }
    }
}
