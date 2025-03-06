package com.divisonapp.service;

import com.divisonapp.dto.Transfer;
import com.divisonapp.model.Participant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DivisionService {
    private List<Participant> participants;

    public List<Transfer> calculateTransfers(List<Participant> participants) {
        this.participants = participants;
        double totalSum = calculateTotalSum();
        double averageSum = totalSum / participants.size();

        List<Participant> difference = calculateDifference(averageSum);
        List<List<Participant>> listUnderpayersAndOverpayers = divideToUnderpayersAndOverpayers(difference);

        return calculateMoneyTransfer(listUnderpayersAndOverpayers.get(0), listUnderpayersAndOverpayers.get(1));
    }

    private double calculateTotalSum() {
        double totalSum = 0;
        for (Participant participant : participants) {
            totalSum += participant.getPayment();
        }
        return totalSum;
    }

    private List<Participant> calculateDifference(double averageSum) {
        List<Participant> difference = new ArrayList<>();
        for (Participant participant : participants) {
            difference.add(new Participant(participant.getName(), participant.getPayment() - averageSum, participant.getEventId()));
        }
        return difference;
    }

    private List<List<Participant>> divideToUnderpayersAndOverpayers(List<Participant> difference) {
        List<Participant> underpayers = new ArrayList<>();
        List<Participant> overpayers = new ArrayList<>();

        for (Participant participant : difference) {
            if (participant.getPayment() < 0) {
                underpayers.add(participant);
            } else if (participant.getPayment() > 0) {
                overpayers.add(participant);
            }
        }

        List<List<Participant>> result = new ArrayList<>();
        result.add(underpayers);
        result.add(overpayers);
        return result;
    }

    private List<Transfer> calculateMoneyTransfer(List<Participant> underpayers, List<Participant> overpayers) {
        List<Transfer> transfers = new ArrayList<>();
        int underpayersIndex = 0;
        int overpayersIndex = 0;

        while (underpayersIndex < underpayers.size() && overpayersIndex < overpayers.size()) {
            Participant underpayer = underpayers.get(underpayersIndex);
            Participant overpayer = overpayers.get(overpayersIndex);

            double transfer = Math.min(underpayer.getPayment() * (-1), overpayer.getPayment());
            transfers.add(new Transfer(underpayer.getName(), overpayer.getName(), transfer));

            underpayer.setPayment(underpayer.getPayment() + transfer);
            overpayer.setPayment(overpayer.getPayment() - transfer);

            if (underpayer.getPayment() == 0) {
                underpayersIndex++;
            }

            if (overpayer.getPayment() == 0) {
                overpayersIndex++;
            }
        }
        return transfers;
    }
}
