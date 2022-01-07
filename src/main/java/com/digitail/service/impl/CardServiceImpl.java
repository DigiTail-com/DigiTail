package com.digitail.service.impl;

import com.digitail.model.Card;
import com.digitail.repos.CardRepository;
import com.digitail.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CardServiceImpl implements IService<Card> {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void save(Card card){
        cardRepository.saveAndFlush(card);
    }

    @Override
    public Collection<Card> findAll() {
        return null;
    }

    @Override
    public Optional<Card> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Card saveOrUpdate(Card card) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
