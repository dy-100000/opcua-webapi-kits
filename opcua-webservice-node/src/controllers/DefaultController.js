/**
 * The DefaultController file is a very simple one, which does not need to be changed manually,
 * unless there's a case where business logic routes the request to an entity which is not
 * the service.
 * The heavy lifting of the Controller item is done in Request.js - that is where request
 * parameters are extracted and sent to the service, and where response is handled.
 */

const Controller = require('./Controller');
const service = require('../services/DefaultService');
const activateSession = async (request, response) => {
  await Controller.handleRequest(request, response, service.activateSession);
};

const browse = async (request, response) => {
  console.log('DefaultController - browse');
  await Controller.handleRequest(request, response, service.browse);
};

const browseNext = async (request, response) => {
  await Controller.handleRequest(request, response, service.browseNext);
};

const call = async (request, response) => {
  await Controller.handleRequest(request, response, service.call);
};

const cancel = async (request, response) => {
  await Controller.handleRequest(request, response, service.cancel);
};

const closeSession = async (request, response) => {
  await Controller.handleRequest(request, response, service.closeSession);
};

const createMonitoredItems = async (request, response) => {
  await Controller.handleRequest(request, response, service.createMonitoredItems);
};

const createSession = async (request, response) => {
  await Controller.handleRequest(request, response, service.createSession);
};

const createSubscription = async (request, response) => {
  await Controller.handleRequest(request, response, service.createSubscription);
};

const deleteMonitoredItems = async (request, response) => {
  await Controller.handleRequest(request, response, service.deleteMonitoredItems);
};

const deleteSubscriptions = async (request, response) => {
  await Controller.handleRequest(request, response, service.deleteSubscriptions);
};

const findServers = async (request, response) => {
  await Controller.handleRequest(request, response, service.findServers);
};

const getEndpoints = async (request, response) => {
  await Controller.handleRequest(request, response, service.getEndpoints);
};

const historyRead = async (request, response) => {
  await Controller.handleRequest(request, response, service.historyRead);
};

const historyUpdate = async (request, response) => {
  await Controller.handleRequest(request, response, service.historyUpdate);
};

const modifyMonitoredItems = async (request, response) => {
  await Controller.handleRequest(request, response, service.modifyMonitoredItems);
};

const modifySubscription = async (request, response) => {
  await Controller.handleRequest(request, response, service.modifySubscription);
};

const publish = async (request, response) => {
  await Controller.handleRequest(request, response, service.publish);
};

const read = async (request, response) => {
  await Controller.handleRequest(request, response, service.read);
};

const registerNodes = async (request, response) => {
  await Controller.handleRequest(request, response, service.registerNodes);
};

const republish = async (request, response) => {
  await Controller.handleRequest(request, response, service.republish);
};

const setMonitoringMode = async (request, response) => {
  await Controller.handleRequest(request, response, service.setMonitoringMode);
};

const setPublishingMode = async (request, response) => {
  await Controller.handleRequest(request, response, service.setPublishingMode);
};

const setTriggering = async (request, response) => {
  await Controller.handleRequest(request, response, service.setTriggering);
};

const transferSubscriptions = async (request, response) => {
  await Controller.handleRequest(request, response, service.transferSubscriptions);
};

const translateBrowsePathsToNodeIds = async (request, response) => {
  await Controller.handleRequest(request, response, service.translateBrowsePathsToNodeIds);
};

const unregisterNodes = async (request, response) => {
  await Controller.handleRequest(request, response, service.unregisterNodes);
};

const write = async (request, response) => {
  await Controller.handleRequest(request, response, service.write);
};


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
