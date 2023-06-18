<script>
	import { onMount } from 'svelte';

	const urlAPI = process.env.URLAPI;
	export let fileCode;
	export let project;
	import Cookie from 'js-cookie';

	async function downloadFile() {
		// Get the JWT token from the cookie
		let jwt = Cookie.get('token');

		// Make the API call
		try {
			let response = await fetch(urlAPI + `/file/downloadFile/${fileCode}`, {
				method: 'GET',
				headers: {
					Authorization: 'Bearer ' + jwt
				}
			});

			let blob = await response.blob(); // Get the response as a Blob object

			// Create a link element
			let a = document.createElement('a');

			// Create a URL for the Blob object
			let url = window.URL.createObjectURL(blob);

			// Set the file name
			let filename = `Cahier des charges ${project.name}.pdf`;

			// Set the attributes of the link element
			a.href = url;
			a.download = filename;

			// Append the link element to the body
			document.body.appendChild(a);

			// Simulate a click on the link element
			a.click();

			// Remove the link element from the body
			document.body.removeChild(a);
		} catch (error) {
			console.error(error);
		}
	}
</script>

<div class="container">
	{#if fileCode !== '' && fileCode !== undefined && fileCode !== null}
		<button on:click={downloadFile}> Télécharger le cahier des charges </button>
	{:else}
		<p>Aucun cahier des charges n'a été ajouté</p>
	{/if}
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: center;
		width: 100%;

		button {
			cursor: pointer;
			border-radius: 10px;
			padding: 10px;
			background-color: #3366ff;
			color: white;
			border: none;
			width: 100%;
			font-size: 1em;

		}
	}
</style>
