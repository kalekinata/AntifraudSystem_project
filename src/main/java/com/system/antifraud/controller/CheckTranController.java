package com.system.antifraud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.system.antifraud.model.Method;
import com.system.antifraud.model.ResponseApi;
import com.system.antifraud.model.db.Accounts;
import com.system.antifraud.model.db.CheckFraud;
import com.system.antifraud.model.db.Client;
import com.system.antifraud.model.db.Transaction;
import com.system.antifraud.model.request.TransactionsRequest;
import com.system.antifraud.repository.AccountRepository;
import com.system.antifraud.repository.CheckFraudRepository;
import com.system.antifraud.repository.ClientRepository;
import com.system.antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class CheckTranController {
    @Value("${bankClient}")
    String bankClient;
    @Autowired
    public TransactionRepository transactionRepository;

    @Autowired
    public CheckFraudRepository checkFraudRepository;

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public ClientRepository clientRepository;

    @GetMapping("/")
    public String home(Model model){
       return "home";
    }

    @GetMapping("/info_tran")
    public String infoTrans(Model model){
        return "";
    }

    @GetMapping("/transaction")
    public String transactionList(Model model){
        Iterable<CheckFraud> checkFrauds = checkFraudRepository.findAllSort();
        model.addAttribute("check", checkFrauds);

        return "tranCheckList";
    }

    @GetMapping("/transaction/{checkid}")
    public String transClient(@PathVariable(value = "checkid") String id, Model model) throws JsonProcessingException {
        if (!checkFraudRepository.existsById(id)){
            return "redirect:/";
        }

        String clid = transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getClidSend();
        String address = bankClient+"info_tran?clid="+clid;
        ResponseApi responseApi = new ResponseApi();
        responseApi = responseApi.sendRequest(Method.GET, null, address);

        System.out.println(responseApi.getSb());

        TransactionsRequest[] transactionsRequest = new Gson().fromJson(responseApi.getSb().toString(), TransactionsRequest[].class);
        model.addAttribute("tranCheck",transactionsRequest);

        Optional<CheckFraud> checkFraud = checkFraudRepository.findById(id);
        ArrayList<CheckFraud> result = new ArrayList<>();
        checkFraud.ifPresent(result::add);
        model.addAttribute("check",result);

        System.out.println(transactionsRequest);
        return "tranDetails";
    }

    @GetMapping("/transaction/check/{checkid}")
    public String tranClientDetails(@PathVariable(value = "checkid") String id, Model model){
        if (!checkFraudRepository.existsById(id)){
            return "redirect:/";
        }

        Optional<CheckFraud> checkFraud = checkFraudRepository.findById(id);
        ArrayList<CheckFraud> result = new ArrayList<>();
        checkFraud.ifPresent(result::add);
        model.addAttribute("check",result);

        Optional<Transaction> transactions = transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid());
        ArrayList<Transaction> resTran = new ArrayList<>();
        transactions.ifPresent(resTran::add);
        model.addAttribute("tran",resTran);

        Optional<Client> clSend = clientRepository.findById(
                transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getClidSend());
        ArrayList<Client> resSend = new ArrayList<>();
        clSend.ifPresent(resSend::add);
        model.addAttribute("clSend",resSend);

        System.out.println(resSend);

        Optional<Client> clRecip = clientRepository.findById(
                transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getClidRecip());
        ArrayList<Client> resRecip = new ArrayList<>();
        clRecip.ifPresent(resRecip::add);
        model.addAttribute("clRecip",resRecip);

        Optional<Accounts> accSend = accountRepository.findById(transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getAccidSend());
        ArrayList<Accounts> resAcSend = new ArrayList<>();
        accSend.ifPresent(resAcSend::add);
        model.addAttribute("accSend",resAcSend);

        Optional<Accounts> accRecip = accountRepository.findByClid(transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getAccidResip());
        ArrayList<Accounts> resAcRecip = new ArrayList<>();
        accRecip.ifPresent(resAcRecip::add);
        model.addAttribute("accSend",resAcRecip);

        return "checkDetails";
    }

    @PostMapping("/transaction/check/{checkid}")
    public String tranClientDetails(@PathVariable(value = "checkid") String id, @RequestParam(value = "button") String description, Model model){
        if (!checkFraudRepository.existsById(id)){
            return "redirect:/";
        }
        String status_check;
        String address = bankClient+"result_check";
        ResponseApi responseApi = new ResponseApi();

        System.out.println(id);
        System.out.println(description);

        switch (description){
            case "green":status_check = "success";
            break;
            case "yellow":status_check = "add_verif";
            break;
            case "red":status_check = "cancel";
            break;
            default: status_check = null;
        }

        System.out.println(status_check);

        int statusFixed = checkFraudRepository.setFixedCheckStatus(id, status_check, description);

        System.out.println(statusFixed);

        CheckFraud checkFraud = checkFraudRepository.findByCheckid(id);
        String json = new Gson().toJson(checkFraud);

        responseApi = responseApi.sendRequest(Method.POST, json, address);

        System.out.println(responseApi.getSb());

        return "redirect:/transaction";
    }

}
