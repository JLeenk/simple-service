package com.moneytransfer.somebackend.defaultimplementation;

import com.moneytransfer.resourcebeans.TransferBean;
import com.moneytransfer.somebackend.ITransferOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultImplementation implements ITransferOperations {

    private static Map<Long, TransferBean> transfers = new HashMap<>();

    @Override
    public List<TransferBean> getAllTransfers() {
        return new ArrayList<>(transfers.values());
    }

    @Override
    public TransferBean getTransferById(long id) {
        return transfers.get(id);
    }

    @Override
    public long createNewTransfer(TransferBean transfer) {
        long transferId = System.currentTimeMillis();
        transfer.setId(transferId);
        transfers.put(transferId, transfer);
        return transferId;
    }

    @Override
    public void updateTransfer(TransferBean transfer) {
        long transferId = transfer.getId();
        transfers.put(transferId, transfer);
    }

    @Override
    public void deleteTransfer(long transferId) {
        transfers.remove(transferId);
    }

    @Override
    public void cleanAll() {
        transfers.clear();
    }

}
