import * as functions from "firebase-functions/v1";
import * as admin from "firebase-admin";

admin.initializeApp();

export const notifyNewMessage = functions.firestore
  .document("messages/{messageId}")
  .onCreate(async (snapshot, context) => {
    const messageId = context.params.messageId;

    let messageDoc;
    try {
      messageDoc = await admin.firestore().collection('messages').doc(messageId).get();
    } catch (error) {
      return;
    }

    const messageData = messageDoc.data();
    if (!messageData) {
      return;
    }

    const { conversationId, senderId } = messageData;

    if (!conversationId || !senderId) {
      return;
    }

    // Dohvat conversation dokumenta
    let conversationDoc;
    try {
      conversationDoc = await admin.firestore().collection('conversations').doc(conversationId).get();
    } catch (error) {
      return;
    }

    if (!conversationDoc.exists) {
      return;
    }

    const conversationData = conversationDoc.data();
    if (!conversationData) {
      return;
    }

    // Određivanje receiverId
    const { participant1Id, participant2Id } = conversationData;
    const receiverId = participant1Id === senderId ? participant2Id : participant1Id;

    if (!receiverId) {
      return;
    }

    // Dohvat receiver dokumenta (za FCM token)
    let receiverDoc;
    try {
      receiverDoc = await admin.firestore().collection('users').doc(receiverId).get();
    } catch (error) {
      return;
    }

    const senderDoc = await admin.firestore().collection('users').doc(messageData.senderId).get();
    const senderName = senderDoc.data()?.name || "Neki korisnik";

    if (!receiverDoc.exists) {
      return;
    }

    const receiverData = receiverDoc.data();
    if (!receiverData) {
      return;
    }

    const fcmToken = receiverData.fcmToken;
    if (!fcmToken) {
      return;
    }

    const sanitizedToken = fcmToken.trim().replace(/\s/g, '');

    // Priprema poruke
    const message: admin.messaging.Message = {
      token: sanitizedToken,
      notification: {
        title: `New message from ${senderName}`,
        body: messageData.text || 'New message',
      },
      data: {
        conversationId: conversationId,
        messageId: messageId,
        type: 'NEW_MESSAGE',
        click_action: 'OPEN_INBOX',
        target_fragment: 'InboxFragment',
        navigation_destination: 'inbox'
      }
    };

    // Slanje poruke
    try {
      await admin.messaging().send(message);
    } catch (error: any) {

    }
  });



export const notifyNewReview = functions.firestore
  .document("reviews/{reviewId}")
  .onCreate(async (snapshot, context) => {
    const reviewId = context.params.reviewId;

    let reviewDoc;
    try {
      reviewDoc = await admin.firestore().collection('reviews').doc(reviewId).get();
    } catch (error) {
      return;
    }

    const reviewData = reviewDoc.data();
    if (!reviewData) {
      return;
    }

    const { userIdFrom, userIdTo, userNameFrom, rating } = reviewData;


    if (!userIdFrom || !userIdTo || !userNameFrom || !rating) {
      return;
    }

    let receiverDoc;
    try {
      receiverDoc = await admin.firestore().collection('users').doc(userIdTo).get();
    } catch (error) {
      return;
    }

    if (!receiverDoc.exists) {
      return;
    }

    const receiverData = receiverDoc.data();
    if (!receiverData) {
      return;
    }

    const fcmToken = receiverData.fcmToken;
    if (!fcmToken) {
      return;
    }

    const sanitizedToken = fcmToken.trim().replace(/\s/g, '');

    const message: admin.messaging.Message = {
      token: sanitizedToken,
      notification: {
        title: 'New review!',
        body: `${userNameFrom} gave you ${rating} ${rating === 1 ? 'star' : 'stars'}`,
      },
      data: {
        reviewId: reviewId,
        userIdFrom: userIdFrom,
        userIdTo: userIdTo,
        type: 'NEW_REVIEW',
        click_action: 'OPEN_REVIEWS',
        target_fragment: 'ReviewFragment',
        navigation_destination: 'reviews'
      }
    };


    try {
      await admin.messaging().send(message);
    } catch (error: any) {

    }
  });
