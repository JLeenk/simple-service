package com.moneytransfer.somebackend;

import com.moneytransfer.resourcebeans.TransferBean;

import java.util.List;

public interface ITransferOperations {
    List<TransferBean> getAllTransfers();
    TransferBean getTransferById(long id);
    long createNewTransfer(TransferBean transfer);
    void updateTransfer(TransferBean transfer);
    void deleteTransfer (long transferId);
    void cleanAll();
}
