package com.example.postItBackend.repository;

import com.example.postItBackend.model.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired EntityManager em;
    
    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    void createMember() {
        int batchSize = 1000;
        for (int i = 0; i < 1000000; i++) {
            String uuid = UUID.randomUUID().toString();
            Member member = Member.builder()
                    .username(uuid + "_member")
                    .nickname(uuid + "_member")
                    .email(uuid + "_member@email.com")
                    .password("password")
                    .loginType("BASIC")
                    .build();

            em.persist(member);

            if (i == 459359){
                System.out.println(uuid + "_member");
            }
            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }

        }
    }

    @Test
    void getMemberByUsernameTest() {
        Member member = memberRepository.findByUsername("9c195303-cb77-4a19-9967-468355ea4967_member")
                .orElseThrow(() -> new IllegalArgumentException("not found"));

        Assertions.assertNotNull(member);
    }

    @Test
    void createMemberWithParallelStream() {
        int batchSize = 1000;
        int totalMembers = 1_000_000;

        List<Integer> indices = IntStream.range(0, totalMembers)
                .boxed()
                .collect(Collectors.toList());

        indices.parallelStream().forEach(i -> {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("your-persistence-unit"); // 실제 Persistence Unit 이름으로 대체
            EntityManager em = emf.createEntityManager();
            EntityTransaction transaction = em.getTransaction();

            try {
                if (i % batchSize == 0) {
                    transaction.begin();
                }

                String uuid = UUID.randomUUID().toString();
                Member member = Member.builder()
                        .username(uuid + "_member")
                        .nickname(uuid + "_member")
                        .email(uuid + "_member@email.com")
                        .password("password")
                        .loginType("BASIC")
                        .build();

                em.persist(member);

                if (i == 459359) {
                    System.out.println(uuid + "_member");
                }

                if (i % batchSize == batchSize - 1 || i == totalMembers - 1) {
                    em.flush();
                    em.clear();
                    transaction.commit();
                }

            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            } finally {
                em.close();
            }
        });
    }
}
