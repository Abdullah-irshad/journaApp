package com.ai.journalApp.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "config_journal_app")
@Data
@NoArgsConstructor
public class ConfigJournalApp {
    @NotNull
    private String key;
    private String value;
}
