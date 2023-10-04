Three changes needed here:
1. Change EmailTransformer class to save mail attachments to com/capgemini/demandforecast/attachments if any. -- DONE
2. Save attachment path to demand repository.  -- DONE
3. Implement Kafka client to send new messages saved in demand repository to Kafka consumer topic.