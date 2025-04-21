package com.divisonapp.service;

import com.divisonapp.dto.Transfer;
import com.divisonapp.model.EventParticipant;
import com.divisonapp.model.Participant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DivisionService {
    private List<EventParticipant> participants;

    public List<Transfer> calculateTransfers(List<EventParticipant> participants) {
        this.participants = participants;
        double totalSum = calculateTotalSum();
        double averageSum = totalSum / participants.size();

        List<EventParticipant> difference = calculateDifference(averageSum);
        List<List<EventParticipant>> listUnderpayersAndOverpayers = divideToUnderpayersAndOverpayers(difference);

        return calculateMoneyTransfer(listUnderpayersAndOverpayers.get(0), listUnderpayersAndOverpayers.get(1));
    }

    private double calculateTotalSum() {
        double totalSum = 0;
        for (EventParticipant participant : participants) {
            totalSum += participant.getPayment();
        }
        return totalSum;
    }

    private List<EventParticipant> calculateDifference(double averageSum) {
        List<EventParticipant> difference = new ArrayList<>();
        for (EventParticipant participant : participants) {
            Participant p = participant.getParticipant();
            difference.add(EventParticipant.builder()
                    .participant(p)
                    .payment(participant.getPayment() - averageSum)
                    .build());
        }
        return difference;
    }

    private List<List<EventParticipant>> divideToUnderpayersAndOverpayers(List<EventParticipant> difference) {
        List<EventParticipant> underpayers = new ArrayList<>();
        List<EventParticipant> overpayers = new ArrayList<>();

        for (EventParticipant participant : difference) {
            if (participant.getPayment() < 0) {
                underpayers.add(participant);
            } else if (participant.getPayment() > 0) {
                overpayers.add(participant);
            }
        }

        List<List<EventParticipant>> result = new ArrayList<>();
        result.add(underpayers);
        result.add(overpayers);
        return result;
    }

    private List<Transfer> calculateMoneyTransfer(List<EventParticipant> underpayers, List<EventParticipant> overpayers) {
        List<Transfer> transfers = new ArrayList<>();
        int underpayersIndex = 0;
        int overpayersIndex = 0;

        while (underpayersIndex < underpayers.size() && overpayersIndex < overpayers.size()) {
            EventParticipant underpayer = underpayers.get(underpayersIndex);
            EventParticipant overpayer = overpayers.get(overpayersIndex);

            double transfer = Math.min(-underpayer.getPayment(), overpayer.getPayment());
            transfers.add(new Transfer(
                    underpayer.getParticipant().getName(),
                    overpayer.getParticipant().getName(),
                    transfer
            ));

            underpayer.setPayment(underpayer.getPayment() + transfer);
            overpayer.setPayment(overpayer.getPayment() - transfer);

            if (Math.abs(underpayer.getPayment()) < 0.01) {
                underpayersIndex++;
            }

            if (Math.abs(overpayer.getPayment()) < 0.01) {
                overpayersIndex++;
            }
        }
        return transfers;
    }
}
