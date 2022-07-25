package com.fastcampus.hellospringbatch.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Getter
@Setter
@Table(name = "plain_text")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class PlainText {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String text;

}
