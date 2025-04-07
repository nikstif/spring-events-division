package com.divisionapp;

import com.divisonapp.dto.Transfer;
import com.divisonapp.model.Participant;
import com.divisonapp.service.DivisionService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DivisionServiceTest {

    private final DivisionService divisionService = new DivisionService();

    @Test
    void testEvenNumberParticipants() {
        List<Participant> participants = List.of(
                new Participant("Name1", 100, 1L),
                new Participant("Name2", 200, 1L)
        );

        List<Transfer> transfers = divisionService.calculateTransfers(participants);
        assertEquals(1, transfers.size());

        Transfer transfer = transfers.get(0);
        assertEquals("Name1", transfer.getFrom());
        assertEquals("Name2", transfer.getTo());
        assertEquals(50.0, transfer.getAmount());
    }

    @Test
    void testOddNumberParticipants() {
        List<Participant> participants = List.of(
                new Participant("Name1", 100, 1L),
                new Participant("Name2", 150, 1L),
                new Participant("Name3", 250, 1L)
        );

        List<Transfer> transfers = divisionService.calculateTransfers(participants);
        assertEquals(2, transfers.size());

        for (Transfer transfer : transfers) {
            if (transfer.getFrom().equals("Name1")) {
                assertEquals("Name3", transfer.getTo());
                assertEquals(66.666, transfer.getAmount(), 0.001);
            } else if (transfer.getFrom().equals("Name2")) {
                assertEquals("Name3", transfer.getTo());
                assertEquals(16.666, transfer.getAmount(), 0.001);
            }
        }
    }

    @Test
    void testLargeDifferencePayments() {
        List<Participant> participants = List.of(
                new Participant("Name1", 10, 1L),
                new Participant("Name2", 1000, 1L)
        );

        List<Transfer> transfers = divisionService.calculateTransfers(participants);

        assertEquals(1, transfers.size());
        Transfer transfer = transfers.get(0);

        assertEquals("Name1", transfer.getFrom());
        assertEquals("Name2", transfer.getTo());
        assertEquals(495.0, transfer.getAmount());
    }

    @Test
    void testSmallDifferencePayments() {
        List<Participant> participants = List.of(
                new Participant("Name1", 99.9, 1L),
                new Participant("Name2", 100.1, 1L)
        );

        List<Transfer> transfers = divisionService.calculateTransfers(participants);

        assertEquals(1, transfers.size());
        Transfer transfer = transfers.get(0);

        assertEquals("Name1", transfer.getFrom());
        assertEquals("Name2", transfer.getTo());
        assertEquals(0.1, transfer.getAmount(), 0.001);
    }
}