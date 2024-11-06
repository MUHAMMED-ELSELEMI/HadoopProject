package com.sau.hadoopassignment.HadoopConfig;


import org.apache.hadoop.fs.FileSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;

@Configuration
public class HdfsConfig {

    @Bean
    public FileSystem hdfsFileSystem() throws IOException {
        org.apache.hadoop.conf.Configuration config = new org.apache.hadoop.conf.Configuration();
        config.set("fs.defaultFS", "hdfs://localhost:9000");
        return FileSystem.get(URI.create("hdfs://localhost:9000"), config);
    }
}
