<script>
	export let data;
	import Navbar from '../Navbar.svelte';
	import { invalidateAll } from '$app/navigation';
	import Cookies from 'js-cookie';

	const urlAPI = process.env.URLAPI;
	let notifications = data.dataNotifications;
	let userInfo = data.userInfo;
	let senders = data.dataSenders;

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}

	//Accepter un consulting
	async function acceptConsulting(idNotification) {
		await fetch(urlAPI + `/teacher/acceptNotification`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json;charset=UTF-8',
				Authorization: `Bearer ` + Cookies.get(`token`)
			},
			body: JSON.stringify({
				idNotification: idNotification
			})
		});
		invalidateAll();
	}

	//refuser un consulting
	async function refuseConsulting(idNotification) {
		await fetch(urlAPI + `/teacher/refuseNotification`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
				Authorization: `Bearer ` + Cookies.get(`token`)
			},
			body: JSON.stringify({
				idNotification: idNotification
			})
		});
		invalidateAll();
	}
</script>

<Navbar {userInfo} />

<div class="container">
	{#if notifications.length === 0}
		<h1>Vous n'avez aucune notification</h1>
	{:else}
		<h1>Vous avez {notifications.length} notification(s)</h1>
	{/if}
	{#each notifications as notification, index}
		<div class="notification">
			<div class="message">{notification.message}</div>
			<div class="sender">
				envoyé par :
				{senders[index].name}
				{senders[index].surname}
				{#if senders[index].idTeam != null}
					(equipe : {senders[index].idTeam})
				{/if}
			</div>
		</div>
		{#if notification.consulting !== null && (userInfo.role === 'ROLE_TEACHER' || userInfo.role === 'ROLE_OPTION_LEADER' || userInfo.role === 'ROLE_JURY_MEMBER')}
			<div class="consultingAcceptOrRefuse">
				<button class="smallButton" on:click={() => acceptConsulting(notification.idNotification)}
					>Accepter</button
				>
				<button class="smallButton" on:click={() => refuseConsulting(notification.idNotification)}
					>Refuser</button
				>
			</div>
		{/if}
	{/each}
</div>

<style>
	.container {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		border-radius: 50px;
		background-color: white;
		color: black;
		padding: 2% 10% 4% 10%;
		margin: 50px 10% 3% 10%;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
	}

	.notification {
		background-color: #f2f2f2;
		border-radius: 5px;
		padding: 10px;
		margin-bottom: 10px;
		text-align: center;
		width: 300px;
	}

	.notification .message {
		font-weight: bold;
		margin-bottom: 5px;
	}

	.notification .sender {
		color: #999;
		font-size: 12px;
	}

	.smallButton {
		padding: 5% 5%;
		text-align: center;
		width: 100%;
		border-radius: 10px;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;
		margin: 2% 5% 15% 0%;
	}

	.smallButton:hover {
		cursor: pointer;
		background-color: #2b5ffb;
		transform: translateY(-2px);
	}

	.consultingAcceptOrRefuse {
		display: flex;
	}
</style>
