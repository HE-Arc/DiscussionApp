package ch.hearc.jee2024.discussionappproject.service;

import ch.hearc.jee2024.discussionappproject.model.Response;
import ch.hearc.jee2024.discussionappproject.repository.DiscussionRepository;
import ch.hearc.jee2024.discussionappproject.repository.ResponseRepository;
import ch.hearc.jee2024.discussionappproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponseService {
    @Autowired
    private final ResponseRepository responseRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseService(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }

    public Optional<Response> getResponseById(Long id) {
        return responseRepository.findById(id);
    }

    public Response createResponse(Response response) {
        return responseRepository.save(response);
    }

    public void deleteResponse(Long id) {
        responseRepository.deleteById(id);
    }

    public List<Response> findResponsesByDiscussionId(Long discussionId) {
        return responseRepository.findByDiscussionId(discussionId);
    }
}

