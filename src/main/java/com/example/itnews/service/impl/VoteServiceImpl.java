package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.IVoteDTO;
import com.example.itnews.entity.Vote;
import com.example.itnews.entity.VoteId;
import com.example.itnews.repository.VoteRepository;
import com.example.itnews.service.VoteService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    private final String TAG = "VoteServiceImpl";
    private final VoteRepository voteRepository;

    public VoteServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public Integer getTotalVoteUp(Integer idAccount) {
        Optional<IVoteDTO> iVoteDTO = voteRepository.getTotalVoteUp(idAccount);
        if (iVoteDTO.isPresent()) return iVoteDTO.get().getTotalVoteUp();
        else return 0;
    }

    @Override
    public Integer getTotalVoteDown(Integer idAccount) {
        Optional<IVoteDTO> iVoteDTO = voteRepository.getTotalVoteDown(idAccount);
        if (iVoteDTO.isPresent()) return iVoteDTO.get().getTotalVoteDown();
        else return 0;
    }

    @Override
    public List<Vote> getUpVotes(Integer idPost) {
        return voteRepository.getUpVotes(idPost)
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<Vote> getDownVotes(Integer idPost) {
        return voteRepository.getDownVotes(idPost)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Boolean isExist(Integer idAccount, Integer idPost) {
        return voteRepository.findById(new VoteId(idAccount, idPost))
                .isPresent();
    }

    @Override
    public Vote getVote(Integer idAccount, Integer idPost) {
        return voteRepository.findById(new VoteId(idAccount, idPost))
                .orElse(new Vote());
    }

    @Override
    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public void delete(Integer idAccount, Integer idPost) {
        voteRepository.deleteById(new VoteId(idAccount, idPost));
    }


}
