package repository;

import com.parkingcomestrue.common.domain.member.Member;
import com.parkingcomestrue.common.domain.member.Password;
import com.parkingcomestrue.common.domain.member.repository.MemberRepository;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BasicMemberRepository implements MemberRepository, BasicRepository<Member, Long> {

    private static Long id = 1L;
    private final Map<Long, Member> store = new HashMap<>();

    @Override
    public boolean existsByEmail(String email) {
        return store.values()
                .stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.of(store.get(id));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return store.values()
                .stream()
                .filter(member -> member.getEmail().equals(email))
                .findAny();
    }

    @Override
    public void save(Member member) {
        setId(member, id);
        store.put(id++, member);
    }

    public List<Member> saveAndGet(int size) {
        LinkedList<Member> result = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            Member member = new Member(
                    "testEmail" + i + "@email.com",
                    "nickname" + i,
                    new Password("password1234")
            );
            result.add(member);
        }
        for (Member member : result) {
            save(member);
        }
        return result;
    }
}
