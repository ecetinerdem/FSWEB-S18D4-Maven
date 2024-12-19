package com.workintech.s18d1.dao;

import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import com.workintech.s18d1.exceptions.BurgerException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class BurgerDaoImpl implements BurgerDao{

    private final EntityManager entityManager;

    @Autowired
    public BurgerDaoImpl(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }


    @Override
    @Transactional
    public Burger save(Burger burger) {
        entityManager.persist(burger);
        return burger;
    }

    @Override
    public Burger findById(Long id) {
        Burger foundBurger = entityManager.find(Burger.class, id);
        if (foundBurger == null) {
            throw new BurgerException("Burger not found " + id, HttpStatus.NOT_FOUND);
        }
        return foundBurger;
    }

    @Override
    public List<Burger> findAll() {
        TypedQuery<Burger>foundAll = entityManager.createQuery("SELECT b FROM Burger b", Burger.class);
        return foundAll.getResultList();
    }

    @Override
    public List<Burger> findByPrice(Integer price) {
        TypedQuery<Burger>foundBurgers = entityManager.createQuery("SELECT b FROM Burger b WHERE b.price > :price ORDER BY b.price DESC", Burger.class);
        foundBurgers.setParameter("price", price);
        return foundBurgers.getResultList();
    }

    @Override
    public List<Burger> findByBreadType(BreadType breadType) {
        TypedQuery<Burger>foundBurgers = entityManager.createQuery("SELECT b FROM Burger b WHERE b.breadType = :breadType ORDER BY b.name DESC", Burger.class);
        foundBurgers.setParameter("breadType", breadType);
        return foundBurgers.getResultList();
    }

    @Override
    public List<Burger> findByContent(String content) {
        TypedQuery<Burger>foundBurgers = entityManager.createQuery("SELECT b FROM Burger b WHERE b.contents LIKE CONCAT ('%', :content, '%') ORDER BY b.name", Burger.class);
        foundBurgers.setParameter("contents", content);
        return foundBurgers.getResultList();
    }

    @Override
    @Transactional
    public Burger update(Burger burger) {
        return entityManager.merge(burger);
    }

    @Override
    @Transactional
    public Burger remove(Long id) {
        Burger foundBurger = findById(id);
        entityManager.remove(foundBurger);
        return foundBurger;
    }
}
