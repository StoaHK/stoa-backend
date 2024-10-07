import unittest
import requests

BASE_URL = "http://localhost:8080/api"
FORUM_URL = f"{BASE_URL}/forum"
AUTH_URL = f"{BASE_URL}/auth"
USER_URL = f"{BASE_URL}/users"

class ForumControllerPaginationTest(unittest.TestCase):
    def setUp(self):
        # Registration and login data
        self.register_data = {
            "username": "testuser",
            "email": "testuser@example.com",
            "password": "testpassword"
        }
        self.login_data = {
            "email": self.register_data["email"],
            "password": self.register_data["password"]
        }

        # Template for creating threads
        self.create_thread_data = {
            "title": "Test Thread",
            "content": "This is a test thread content with enough length."
        }
        
        # Total number of threads to create for pagination test
        self.total_threads = 25  # Can be set higher if more pages are desired

        # Step 1: Register and login to get JWT token
        self._register_user()
        self.token = self._login_user()

        # Headers with authorization
        self.headers = {"Authorization": f"Bearer {self.token}"}

    def _register_user(self):
        # Register the test user
        requests.post(f"{USER_URL}/register", json=self.register_data)

    def _login_user(self):
        # Log in and get a token
        login_response = requests.post(f"{AUTH_URL}/login", json=self.login_data)
        self.assertEqual(login_response.status_code, 200, "Login failed")
        return login_response.json().get("token")

    def test_thread_creation_pagination_and_deletion(self):
        created_thread_ids = []

        # Step 2: Create `self.total_threads` threads
        for i in range(self.total_threads):
            thread_data = {
                "title": f"{self.create_thread_data['title']} {i+1}",
                "content": self.create_thread_data["content"]
            }
            create_response = requests.post(f"{FORUM_URL}/threads", json=thread_data, headers=self.headers)
            self.assertEqual(create_response.status_code, 201, f"Failed to create thread {i+1}")
            created_thread_ids.append(create_response.json().get("id"))

        # Step 3: Verify pagination with different page sizes
        page_size = 5  # Example page size for testing
        total_pages = (self.total_threads + page_size - 1) // page_size  # Calculate expected total pages

        for page in range(total_pages):
            # Fetch each page of threads
            threads_response = requests.get(f"{FORUM_URL}/threads?page={page}&size={page_size}", headers=self.headers)
            self.assertEqual(threads_response.status_code, 200, f"Failed to fetch page {page} of threads")
            threads_data = threads_response.json()

            # Assert pagination metadata
            self.assertEqual(threads_data.get("pageable").get("pageNumber"), page, f"Expected page {page}")
            self.assertEqual(threads_data.get("pageable").get("pageSize"), page_size, f"Expected page size {page_size}")
            self.assertEqual(threads_data.get("totalPages"), total_pages, f"Expected total pages {total_pages}")

            # Assert thread count per page (except possibly the last page)
            if page < total_pages - 1:
                self.assertEqual(len(threads_data.get("content")), page_size, f"Expected {page_size} threads on page {page}")
            else:
                # Last page may have fewer threads
                expected_last_page_size = self.total_threads % page_size or page_size
                self.assertEqual(len(threads_data.get("content")), expected_last_page_size, "Unexpected thread count on the last page")

        # Step 4: Clean up by deleting all created threads
        for thread_id in created_thread_ids:
            delete_response = requests.delete(f"{FORUM_URL}/threads/{thread_id}", headers=self.headers)
            self.assertEqual(delete_response.status_code, 204, f"Failed to delete thread ID {thread_id}")

        # Verify all threads are deleted by checking if no threads remain
        remaining_threads_response = requests.get(f"{FORUM_URL}/threads?page=0&size={self.total_threads}", headers=self.headers)
        self.assertEqual(remaining_threads_response.status_code, 200, "Failed to fetch threads list after deletion")
        remaining_threads = remaining_threads_response.json().get("content")
        self.assertEqual(len(remaining_threads), 0, "Threads were not deleted as expected")

    def tearDown(self):
        # Cleanup: delete the user created during tests
        requests.delete(f"{USER_URL}/me", headers=self.headers)

if __name__ == "__main__":
    unittest.main()
