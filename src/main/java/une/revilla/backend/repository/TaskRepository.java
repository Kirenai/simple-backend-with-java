package une.revilla.backend.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import une.revilla.backend.entity.Task;

@Repository
@Qualifier("taskRepository")
public interface TaskRepository extends JpaRepository<Task, Long> {
}
