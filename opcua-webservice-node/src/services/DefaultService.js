/* eslint-disable no-unused-vars */
const { uaServerApi } = require('../server');
const Service = require('./Service');

/**
*
* activateSessionRequest ActivateSessionRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.3/#5.7.3.2). (optional)
* returns ActivateSessionResponse
* */
const activateSession = ({ activateSessionRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        activateSessionRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* browseRequest BrowseRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.2/#5.9.2.2). (optional)
* returns BrowseResponse
* */
const browse = ({ browseRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        browseRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* browseNextRequest BrowseNextRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.3/#5.9.3.2). (optional)
* returns BrowseNextResponse
* */
const browseNext = ({ browseNextRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        browseNextRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* callRequest CallRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2). (optional)
* returns CallResponse
* */
const call = ({ path, callRequest, callRequestWithPathRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      const req = callRequest || callRequestWithPathRequest;
      const responseData = await uaServerApi.call(req, path);
      resolve(Service.successResponse(responseData));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* cancelRequest CancelRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.5/#5.7.5.2). (optional)
* returns CancelResponse
* */
const cancel = ({ cancelRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        cancelRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* closeSessionRequest CloseSessionRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.4/#5.7.4.2). (optional)
* returns CloseSessionResponse
* */
const closeSession = ({ closeSessionRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        closeSessionRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* createMonitoredItemsRequest CreateMonitoredItemsRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2). (optional)
* returns CreateMonitoredItemsResponse
* */
const createMonitoredItems = ({ createMonitoredItemsRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        createMonitoredItemsRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* createSessionRequest CreateSessionRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.2/#5.7.2.2). (optional)
* returns CreateSessionResponse
* */
const createSession = ({ createSessionRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        createSessionRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* createSubscriptionRequest CreateSubscriptionRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.2/#5.14.2.2). (optional)
* returns CreateSubscriptionResponse
* */
const createSubscription = ({ createSubscriptionRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        createSubscriptionRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* deleteMonitoredItemsRequest DeleteMonitoredItemsRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.6/#5.13.6.2). (optional)
* returns DeleteMonitoredItemsResponse
* */
const deleteMonitoredItems = ({ deleteMonitoredItemsRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        deleteMonitoredItemsRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* deleteSubscriptionsRequest DeleteSubscriptionsRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.8/#5.14.8.2). (optional)
* returns DeleteSubscriptionsResponse
* */
const deleteSubscriptions = ({ deleteSubscriptionsRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        deleteSubscriptionsRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* findServersRequest FindServersRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.2/#5.5.2.2). (optional)
* returns FindServersResponse
* */
const findServers = ({ findServersRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      const responseData = await uaServerApi.findServers(findServersRequest);
      resolve(Service.successResponse(responseData));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* getEndpointsRequest GetEndpointsRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.4/#5.5.4.2). (optional)
* returns GetEndpointsResponse
* */
const getEndpoints = ({ path,getEndpointsRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      const responseData = await uaServerApi.getEndpoints(getEndpointsRequest, path);
      resolve(Service.successResponse(responseData));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  }
);
/**
*
* historyReadRequest HistoryReadRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2). (optional)
* returns HistoryReadResponse
* */
const historyRead = ({ path, historyReadRequest, historyReadWithPathRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      const req = historyReadRequest || historyReadWithPathRequest;
      const responseData = await uaServerApi.historyRead(req, path);
      resolve(Service.successResponse(responseData));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* historyUpdateRequest HistoryUpdateRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.5/#5.11.5.2). (optional)
* returns HistoryUpdateResponse
* */
const historyUpdate = ({ path, historyUpdateRequest, historyUpdateWithPathRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      const req = historyUpdateRequest || historyUpdateWithPathRequest;
      const responseData = await uaServerApi.historyUpdate(req, path);
      resolve(Service.successResponse(responseData));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* modifyMonitoredItemsRequest ModifyMonitoredItemsRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.3/#5.13.3.2). (optional)
* returns ModifyMonitoredItemsResponse
* */
const modifyMonitoredItems = ({ modifyMonitoredItemsRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        modifyMonitoredItemsRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* modifySubscriptionRequest ModifySubscriptionRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.3/#5.14.3.2). (optional)
* returns ModifySubscriptionResponse
* */
const modifySubscription = ({ modifySubscriptionRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        modifySubscriptionRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* publishRequest PublishRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2). (optional)
* returns PublishResponse
* */
const publish = ({ publishRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        publishRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* readRequest ReadRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.2/#5.11.2.2). (optional)
* returns ReadResponse
* */
const read = ({ path, readRequest, readWithPathRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      const req = readRequest || readWithPathRequest;
      const responseData = await uaServerApi.read(req, path);
      resolve(Service.successResponse(responseData));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  }
);
/**
*
* registerNodesRequest RegisterNodesRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.5/#5.9.5.2). (optional)
* returns RegisterNodesResponse
* */
const registerNodes = ({ registerNodesRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        registerNodesRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* republishRequest RepublishRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.6/#5.14.6.2). (optional)
* returns RepublishResponse
* */
const republish = ({ republishRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        republishRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* setMonitoringModeRequest SetMonitoringModeRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.4/#5.13.4.2). (optional)
* returns SetMonitoringModeResponse
* */
const setMonitoringMode = ({ setMonitoringModeRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        setMonitoringModeRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* setPublishingModeRequest SetPublishingModeRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.4/#5.14.4.2). (optional)
* returns SetPublishingModeResponse
* */
const setPublishingMode = ({ setPublishingModeRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        setPublishingModeRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* setTriggeringRequest SetTriggeringRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.5/#5.13.5.2). (optional)
* returns SetTriggeringResponse
* */
const setTriggering = ({ setTriggeringRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        setTriggeringRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* transferSubscriptionsRequest TransferSubscriptionsRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.7/#5.14.7.2). (optional)
* returns TransferSubscriptionsResponse
* */
const transferSubscriptions = ({ transferSubscriptionsRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        transferSubscriptionsRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* translateBrowsePathsToNodeIdsRequest TranslateBrowsePathsToNodeIdsRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2). (optional)
* returns TranslateBrowsePathsToNodeIdsResponse
* */
const translateBrowsePathsToNodeIds = ({ translateBrowsePathsToNodeIdsRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        translateBrowsePathsToNodeIdsRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* unregisterNodesRequest UnregisterNodesRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.6/#5.9.6.2). (optional)
* returns UnregisterNodesResponse
* */
const unregisterNodes = ({ unregisterNodesRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        unregisterNodesRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);
/**
*
* writeRequest WriteRequest [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.4/#5.11.4.2). (optional)
* returns WriteResponse
* */
const write = ({ writeRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      resolve(Service.successResponse({
        writeRequest,
      }));
    } catch (e) {
      reject(Service.rejectResponse(
        e.message || 'Invalid input',
        e.status || 405,
      ));
    }
  },
);

module.exports = {
  activateSession,
  browse,
  browseNext,
  call,
  cancel,
  closeSession,
  createMonitoredItems,
  createSession,
  createSubscription,
  deleteMonitoredItems,
  deleteSubscriptions,
  findServers,
  getEndpoints,
  historyRead,
  historyUpdate,
  modifyMonitoredItems,
  modifySubscription,
  publish,
  read,
  registerNodes,
  republish,
  setMonitoringMode,
  setPublishingMode,
  setTriggering,
  transferSubscriptions,
  translateBrowsePathsToNodeIds,
  unregisterNodes,
  write,
};
